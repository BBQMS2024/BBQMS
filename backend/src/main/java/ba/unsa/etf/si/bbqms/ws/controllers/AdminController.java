package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
<<<<<<< HEAD
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.ErrorMessage;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
=======
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.exceptions.AuthException;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.util.stream.Collectors;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74

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

<<<<<<< HEAD
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity getAdmins(@PathVariable(name = "code") final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't get admins"));
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
=======
    @PostMapping("/{code}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN', 'ROLE_STAFF_ADMIN')")
    public ResponseEntity getUsers(@RequestBody RoleRequest request, @PathVariable(name = "code") final String tenantCode) {
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.roleName);
        } catch (IllegalArgumentException e) {
            logger.warn("Role doesn't exist");
            return ResponseEntity.badRequest().build();
        }

        if(this.authService.canOnlyCRUDUser(roleName)){
            logger.warn("Only super admin can read admins");
            return ResponseEntity.badRequest().build();
        }

        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok().body(
                    this.adminService.findUsersByCode(tenantCode, request.roleName).stream()
                            .map(UserDto::fromEntity)
                            .collect(Collectors.toList())
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        }
    }

    @PostMapping("/{code}/user")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity addAdmin(@RequestBody final AdminRequest request, @PathVariable(name = "code") final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't add admin"));
        }
        final User newAdmin = User.builder()
=======
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN', 'ROLE_STAFF_ADMIN')")
    public ResponseEntity addUser(@RequestBody final AdminRequest request, @PathVariable(name = "code") final String tenantCode) throws AuthException {
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(request.roleName);
        } catch (IllegalArgumentException e) {
            logger.warn("Role doesn't exist");
            return ResponseEntity.badRequest().build();
        }

        if(this.authService.canOnlyCRUDUser(roleName)){
            logger.warn("Only super admin can add admin");
            return ResponseEntity.badRequest().build();
        }

        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }

        final User newUser = User.builder()
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
                .password(request.password())
                .email(request.email)
                .build();
        try {
<<<<<<< HEAD
            final User user = this.adminService.addAdmin(newAdmin, tenantCode);
            return ResponseEntity.ok(UserDto.fromEntity(user));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't add admin: " + newAdmin.getUsername()));
=======
            final User user = this.adminService.addUser(newUser, tenantCode, request.roleName);
            return ResponseEntity.ok(UserDto.fromEntity(user));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        }
    }

    @PutMapping("/{code}/user/{userId}")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity updateAdmin(@RequestBody final UserDto admin, @PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't update admin"));
        }

        try {
            final User user = this.adminService.updateAdmin(admin, tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Updated admin: " + user.getUsername()));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't update admin"));
=======
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN', 'ROLE_STAFF_ADMIN')")
    public ResponseEntity updateAdmin(@RequestBody final UserDto request, @PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }

        try {
            final User user = this.adminService.updateUser(request, tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Updated user: " + user.getUsername()));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        }
    }

    @DeleteMapping("/{code}/user/{userId}")
<<<<<<< HEAD
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity removeAdmin(@PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            logger.warn("Super admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't remove admin"));
        }

        try {
            this.adminService.removeAdmin(tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Removed admin"));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().body(new ErrorMessage("Couldn't remove admin user"));
        }
    }

    public record AdminRequest(String email, String password) {
=======
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN', 'ROLE_STAFF_ADMIN')")
    public ResponseEntity removeAdmin(@PathVariable(name = "code") final String tenantCode, @PathVariable(name = "userId") final long adminId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            logger.warn("Admin does not belong to the specified tenant");
            return ResponseEntity.badRequest().build();
        }

        try {
            this.adminService.removeUser(tenantCode, adminId);
            return ResponseEntity.ok().body(new SimpleMessageDto("Removed user"));
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    public record AdminRequest(String email, String password, String roleName) {
    }

    public record RoleRequest(String roleName){
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    }
}
