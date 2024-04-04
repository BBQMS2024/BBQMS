package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.ws.models.DisplayDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/displays")
public class DisplayController {
    private final DisplayService displayService;
    private final AuthService authService;

    public DisplayController(DisplayService displayService, AuthService authService) {
        this.displayService = displayService;
        this.authService = authService;
    }

    @PostMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity createDisplay(@PathVariable final String tenantCode,
                                        @RequestBody final DisplayCreateRequest request) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Display createdDisplay = this.displayService.createDisplay(request.name(), request.branchId());
            return ResponseEntity.ok().body(DisplayDto.fromEntity(createdDisplay));
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getDisplays(@PathVariable final String tenantCode,
                                      @PathVariable final String branchId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Set<DisplayDto> displays = this.displayService.getDisplays(Long.parseLong(branchId)).stream()
                    .map(DisplayDto::fromEntity)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok().body(displays);
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getDisplaysByTenant(@PathVariable final String tenantCode) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Set<DisplayDto> displays = this.displayService.getDisplaysByTenant(tenantCode).stream()
                    .map(DisplayDto::fromEntity)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok().body(displays);
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{tenantCode}/{displayId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateDisplay(@PathVariable final String tenantCode,
                                        @PathVariable final String displayId,
                                        @RequestBody final DisplayUpdateRequest request) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try{
            final Display updatedDisplay = this.displayService.updateDisplay(Long.parseLong(displayId), request.name());
            return ResponseEntity.ok().body(DisplayDto.fromEntity(updatedDisplay));
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tenantCode}/{displayId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity removeDisplay(@PathVariable final String tenantCode,
                                        @PathVariable final String displayId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        this.displayService.removeDisplay(Long.parseLong(displayId));

        return ResponseEntity.ok().body(new SimpleMessageDto("Deleted display with id: " + displayId));
    }

    public record DisplayCreateRequest(String name, long branchId){
    }

    public record DisplayUpdateRequest(String name){
    }
}
