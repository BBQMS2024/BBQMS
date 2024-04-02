package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.ErrorMessage;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AdminService adminService;
    private AuthService authService;

    public AdminController(final AdminService adminService, final AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity getAdmins(@PathVariable(name = "code") final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            final List<User> admins = this.adminService.findAdminsByCode(tenantCode);
            List<UserDto> adminDtos = new ArrayList<>();

            for (User admin : admins) {
                adminDtos.add(UserDto.fromEntity(admin));
            }
            return ResponseEntity.ok(adminDtos);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PostMapping("/{code}/user")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity addAdmin(@RequestBody final AdminRequest request, @PathVariable(name = "code") final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }
        final User newAdmin = User.builder()
                .password(request.password())
                .email(request.email)
                .build();
        try {
            final User user = this.adminService.addAdmin(newAdmin, tenantCode);
            return ResponseEntity.ok(UserDto.fromEntity(user));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't add admin user: " + newAdmin.getUsername()));
        }
    }

    public record AdminRequest(String email, String password) {
    }
}
