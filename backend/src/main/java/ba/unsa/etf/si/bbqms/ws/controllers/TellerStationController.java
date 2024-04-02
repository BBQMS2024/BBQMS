package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.TellerStationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stations")
public class TellerStationController {
    private final StationService stationService;
    private final AuthService authService;

    public TellerStationController(final StationService stationService,
                                   final AuthService authService) {
        this.stationService = stationService;
        this.authService = authService;
    }

    @PutMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addTellerStationService(@PathVariable final String tenantCode, @PathVariable final String stationId, @PathVariable final String serviceId) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.addTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteTellerStationService(@PathVariable final String tenantCode, @PathVariable final String stationId, @PathVariable final String serviceId) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.deleteTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
