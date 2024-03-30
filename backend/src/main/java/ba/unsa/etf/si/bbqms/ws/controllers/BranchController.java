package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.TellerStation;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.BranchDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.TellerStationDto;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {
    private final AuthService authService;
    private final BranchService branchService;

    public BranchController(final AuthService authService, final BranchService branchService) {
        this.authService = authService;
        this.branchService = branchService;
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
            return ResponseEntity.ok().body(BranchDto.fromEntity(created));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getBranches(@PathVariable final String tenantCode) {
        final User currentUser = this.authService.getAuthenticatedUser();
        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        final Set<BranchDto> dtos = this.branchService.getBranches(tenantCode).stream()
                .map(BranchDto::fromEntity)
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
            return ResponseEntity.ok().body(BranchDto.fromEntity(updated));
        } catch (final Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{tenantCode}/{branchId}/stations")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addStation(@RequestBody final StationRequest request,
                                     @PathVariable final String tenantCode,
                                     @PathVariable final String branchId) {
        final User currentUser = this.authService.getAuthenticatedUser();
        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
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
        final User currentUser = this.authService.getAuthenticatedUser();
        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
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
        final User currentUser = this.authService.getAuthenticatedUser();
        if (!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            this.branchService.removeStation(Long.parseLong(stationId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Removed station."));
        } catch (final Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record BranchRequest(String name, List<String> tellerStations) {
    }

    public record StationRequest(String name) {
    }
}
