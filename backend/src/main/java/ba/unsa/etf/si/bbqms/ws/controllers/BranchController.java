package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ticket_service.api.TicketService;
import ba.unsa.etf.si.bbqms.ws.models.*;
import ba.unsa.etf.si.bbqms.ws.params.branch.QueueStateParams;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {
    private final AuthService authService;
    private final BranchService branchService;
    private final TicketService ticketService;
    private final TenantService tenantService;

    public BranchController(final AuthService authService,
                            final BranchService branchService,
                            final TicketService ticketService,
                            final TenantService tenantService) {
        this.authService = authService;
        this.branchService = branchService;
        this.ticketService = ticketService;
        this.tenantService = tenantService;
    }

    @PostMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity createBranch(@RequestBody final BranchRequest request,
                                       @PathVariable final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();

        if (!currentUser.getTenant().getCode().equals(tenantCode) ||
                request.name == null ||
                request.name.trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        try {
            final Branch created = this.branchService.createBranch(request.name(), request.tellerStations(), tenantCode);
            return ResponseEntity.ok().body(BranchWithStationsDto.fromEntity(created));
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}")
    public ResponseEntity getBranches(@PathVariable final String tenantCode) {
        final Set<BranchWithStationsDto> dtos = this.branchService.getBranches(tenantCode).stream()
                .map(BranchWithStationsDto::fromEntity)
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(dtos);
    }

    @PutMapping("/{tenantCode}/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateBranch(@RequestBody final BranchRequest request,
                                       @PathVariable final String tenantCode,
                                       @PathVariable final String branchId) {
        final User currentUser = this.authService.getAuthenticatedUser();
        if (!currentUser.getTenant().getCode().equals(tenantCode) ||
                request.name == null ||
                request.name.trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        try {
            final Branch updated = this.branchService.updateBranch(
                    Long.parseLong(branchId),
                    request.name(),
                    request.tellerStations(),
                    tenantCode
            );
            return ResponseEntity.ok().body(BranchWithStationsDto.fromEntity(updated));
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tenantCode}/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity removeBranch(@PathVariable final String tenantCode, @PathVariable final String branchId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        this.branchService.removeBranch(Long.parseLong(branchId));

        return ResponseEntity.ok().body(new SimpleMessageDto("Deleted branch with id: " + branchId));
    }

    @PostMapping("/{tenantCode}/{branchId}/stations")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addStation(@RequestBody final StationRequest request,
                                     @PathVariable final String tenantCode,
                                     @PathVariable final String branchId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation created = this.branchService.addStation(request.name(), Long.parseLong(branchId));
            return ResponseEntity.ok().body(TellerStationDto.fromEntity(created));
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{tenantCode}/{branchId}/stations/{stationId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateStation(@RequestBody StationRequest request,
                                        @PathVariable final String tenantCode,
                                        @PathVariable final String branchId,
                                        @PathVariable final String stationId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final TellerStation updated = this.branchService.updateStation(Long.parseLong(stationId), request.name());
            return ResponseEntity.ok().body(TellerStationDto.fromEntity(updated));
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tenantCode}/{branchId}/stations/{stationId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity removeStation(@PathVariable final String tenantCode,
                                        @PathVariable final String branchId,
                                        @PathVariable final String stationId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            this.branchService.removeStation(Long.parseLong(stationId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Removed station."));
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}/{branchId}/services")
    public ResponseEntity getBranchServices(@PathVariable final String branchId,
                                            @PathVariable final String tenantCode) {
        return this.branchService.findById(Long.parseLong(branchId))
                .map(this.branchService::extractPossibleServices)
                .map(services -> services.stream().map(ServiceDto::fromEntity).collect(Collectors.toSet()))
                .map(serviceDtos -> ResponseEntity.ok().body(serviceDtos))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{tenantCode}/{branchId}/queue")
    public ResponseEntity getBranchQueue(@PathVariable final String tenantCode,
                                         @PathVariable final String branchId,
                                         final QueueStateParams queueStateParams,
                                         final Sort sort) {
        final Optional<Branch> optionalBranch = this.branchService.findById(Long.parseLong(branchId));
        if (optionalBranch.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        final Branch branch = optionalBranch.get();

        final Set<Service> services;
        if (queueStateParams.serviceId().isPresent()) {
            services = Set.of(this.tenantService.getServiceById(queueStateParams.serviceId().get()));
        } else {
            services = this.branchService.extractPossibleServices(branch);
        }

        return ResponseEntity.ok().body(
                this.ticketService.findAllByServicesAndBranch(services, branch, sort).stream()
                        .map(TicketDto::fromEntity)
                        .toList()
        );
    }

    public record BranchRequest(String name, List<String> tellerStations) {
    }

    public record StationRequest(String name) {
    }
}
