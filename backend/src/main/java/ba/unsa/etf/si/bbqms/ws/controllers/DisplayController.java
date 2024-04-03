package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.ws.models.DisplayDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            final Display createdDisplay = this.displayService.createDisplay(request.name(),
                    request.tellerStationId());
            return ResponseEntity.ok().body(DisplayDto.fromEntity(createdDisplay));
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}/{displayId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getDisplay(@PathVariable final String tenantCode,
                                     @PathVariable final String displayId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try{
            Display display = this.displayService.getDisplay(Long.parseLong(displayId));
            return ResponseEntity.ok().body(DisplayDto.fromEntity(display));
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
            Display updated = this.displayService.updateDisplay(Long.parseLong(displayId), request.name());
            return ResponseEntity.ok().body(DisplayDto.fromEntity(updated));
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

    public record DisplayCreateRequest(String name, long tellerStationId){
    }

    public record DisplayUpdateRequest(String name){
    }
}
