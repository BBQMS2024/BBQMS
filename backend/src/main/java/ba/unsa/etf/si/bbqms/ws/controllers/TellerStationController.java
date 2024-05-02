package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.StationService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.Ticket;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import ba.unsa.etf.si.bbqms.ws.models.DisplayDto;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.ServiceResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.TicketDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stations")
public class TellerStationController {
    private final StationService stationService;
    private final AuthService authService;
    private final TicketService ticketService;

    public TellerStationController(final StationService stationService,
                                   final AuthService authService,
                                   final TicketService ticketService) {
        this.stationService = stationService;
        this.authService = authService;
        this.ticketService = ticketService;
    }

    @GetMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getAll(@PathVariable final String tenantCode){
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final List<TellerStation> tellerStationList = this.stationService.getAllByTenant(tenantCode);
            final List<TellerStationResponseDto> tellerStationResponseDtoList = tellerStationList.stream()
                    .map(TellerStationResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(tellerStationResponseDtoList);
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @GetMapping("/{tenantCode}/{stationId}/services")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getServices(@PathVariable final String tenantCode,
                                      @PathVariable final String stationId,
                                      @RequestParam(defaultValue = "true") final boolean assigned) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final Set<Service> serviceSet = this.stationService.getServicesByAssigned(Long.parseLong(stationId),assigned);
            final Set<ServiceResponseDto> serviceResponseDtoSet = serviceSet.stream()
                    .map(ServiceResponseDto::fromEntity)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok().body(serviceResponseDtoSet);
        }
        catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addTellerStationService(@PathVariable final String tenantCode,
                                                  @PathVariable final String stationId,
                                                  @PathVariable final String serviceId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.addTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tenantCode}/{stationId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteTellerStationService(@PathVariable final String tenantCode,
                                                     @PathVariable final String stationId,
                                                     @PathVariable final String serviceId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.deleteTellerStationService(Long.parseLong(stationId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{stationId}/displays/{displayId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addDisplayToStation(@PathVariable final String tenantCode,
                                              @PathVariable final String stationId,
                                              @PathVariable final String displayId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.addTellerStationDisplay(Long.parseLong(stationId), Long.parseLong(displayId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch(final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tenantCode}/{stationId}/displays/{displayId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity removeDisplayFromStation(@PathVariable final String tenantCode,
                                                   @PathVariable final String stationId,
                                                   @PathVariable final String displayId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updatedTellerStation = this.stationService.deleteTellerStationDisplay(Long.parseLong(stationId), Long.parseLong(displayId));
            return ResponseEntity.ok().body(TellerStationResponseDto.fromEntity(updatedTellerStation));
        } catch(final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getBranchStations(@PathVariable final String tenantCode,
                                            @PathVariable final String branchId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try{
            return ResponseEntity.ok().body(
                    this.stationService.getAllByBranch(Long.parseLong(branchId)).stream()
                            .map(TellerStationResponseDto::fromEntity)
                            .collect(Collectors.toList())
            );
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{stationId}/tickets")
    public ResponseEntity getTicketsForTellerStation(@PathVariable long stationId) {
        try {
            Set<Service> services = stationService.getServicesForTellerStation(stationId);
            Set<Ticket> tickets = ticketService.getTicketsForServices(services);
            List<TicketDto> ticketDtos = tickets.stream()
                    .map(TicketDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(ticketDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record TellerStationResponseDto(long id, String name, DisplayDto display, Set<ServiceResponseDto> services) {
        public static TellerStationResponseDto fromEntity(final TellerStation tellerStation) {
            final Set<Service> serviceSet = tellerStation.getServices();
            return new TellerStationResponseDto(
                    tellerStation.getId(),
                    tellerStation.getName(),
                    tellerStation.getDisplay() != null ? DisplayDto.fromEntity(tellerStation.getDisplay()) : null,
                    serviceSet.stream().map(ServiceResponseDto::fromEntity).collect(Collectors.toSet())
            );
        }
    }
}
