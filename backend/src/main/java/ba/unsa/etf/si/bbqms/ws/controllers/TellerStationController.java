package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stations")
public class TellerStationController {
    private final StationService stationService;
    private final AuthService authService;
    public TellerStationController(StationService stationService, AuthService authService) {
        this.stationService = stationService;
        this.authService = authService;
    }

    @PutMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addTellerStationService(@PathVariable final String tenantCode, @PathVariable final String stationId, @PathVariable final String serviceId) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            stationService.addTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Successfully added service to teller station"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteTellerStationService(@PathVariable final String tenantCode, @PathVariable final String stationId, @PathVariable final String serviceId) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            stationService.deleteTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Successfully removed service from teller station"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
