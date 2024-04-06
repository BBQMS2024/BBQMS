package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AdminService adminService;
    private final AuthService authService;

    public AdminController(final AdminService adminService, final AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity getAdmins(@RequestBody RoleRequest request, @PathVariable(name = "code") final String tenantCode) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok().body(
                    this.adminService.findAdminsByCode(tenantCode, request.roleNames).stream()
                            .map(UserDto::fromEntity)
                            .collect(Collectors.toList())
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{code}/user")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity addAdmin(@RequestBody final AdminRequest request, @PathVariable(name = "code") final String tenantCode) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }
        final User newAdmin = User.builder()
                .password(request.password())
                .email(request.email)
                .build();
        try {
            final User user = this.adminService.addAdmin(newAdmin, tenantCode, request.roleName);
            return ResponseEntity.ok(UserDto.fromEntity(user));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{code}/user/{userId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity updateAdmin(@RequestBody final UserDto admin, @PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }

        try {
            final User user = this.adminService.updateAdmin(admin, tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Updated admin: " + user.getUsername()));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{code}/user/{userId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity removeAdmin(@PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }

        try {
            this.adminService.removeAdmin(tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Removed admin"));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    public record AdminRequest(String email, String password, String roleName) {
    }

    public record RoleRequest(List<String> roleNames){
    }
}
