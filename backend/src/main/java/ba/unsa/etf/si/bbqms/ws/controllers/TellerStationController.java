package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stations")
public class TellerStationController {
    private final StationService stationService;

    public TellerStationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PutMapping("/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addTellerStationService(@PathVariable String stationId, @PathVariable String serviceId) {
        try {
            stationService.addTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Successfully added service to teller station"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteTellerStationService(@PathVariable String stationId, @PathVariable String serviceId) {
        try {
            stationService.deleteTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Successfully removed service from teller station"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
