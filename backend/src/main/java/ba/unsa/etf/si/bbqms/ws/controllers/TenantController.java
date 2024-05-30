package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.ekapic1.stonebase.filter.ConditionType;
import ba.ekapic1.stonebase.filter.FilterBuilder;
import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.ServiceField;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import ba.unsa.etf.si.bbqms.ws.models.ServiceRequestDto;
import ba.unsa.etf.si.bbqms.ws.models.SimpleMessageDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import ba.unsa.etf.si.bbqms.ws.params.service.ServiceListParams;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private final TenantService tenantService;
    private final BranchService branchService;
    private final GroupService groupService;
    private final AuthService authService;

    public TenantController(final TenantService tenantService,
                            final BranchService branchService,
                            final GroupService groupService,
                            final AuthService authService) {
        this.tenantService = tenantService;
        this.branchService = branchService;
        this.groupService = groupService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity addTenant(@RequestBody final TenantDto tenantDto) {
        return ResponseEntity.ok().body(tenantService.addTenant(tenantDto));
    }

    @GetMapping("/{code}")
    public ResponseEntity getTenantByCode(@PathVariable(name = "code") final String code) {
        try {
            return ResponseEntity.ok().body(tenantService.findByCode(code));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity updateTenant(@PathVariable final String code, @RequestBody final TenantDto request) throws Exception {
        if (!this.authService.canChangeTenant(code)) {
            return ResponseEntity.notFound().build();
        }

        try {
            return ResponseEntity.ok().body(tenantService.updateTenant(code, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{code}/services")
    @PreAuthorize("hasAnyRole('ROLE_BRANCH_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity addService(@PathVariable(name = "code") final String code, @RequestBody final ServiceRequestDto serviceRequestDto) {
        if (!this.authService.canChangeTenant(code)) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok().body(tenantService.addService(code, serviceRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{code}/services")
    public ResponseEntity getAllServicesByTenant(@PathVariable(name = "code") final String code) {
        try {
            return ResponseEntity.ok().body(tenantService.getAllServicesByTenant(code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{code}/services/group/{groupId}")
    public ResponseEntity listGroupAssignableServices(@PathVariable final String code, @PathVariable final String groupId) {
        final ServiceRepository serviceRepository = this.tenantService.unwrap(ServiceRepository.class);

        final List<Service> possibleServices = this.tenantService.getAllServicesByTenant(code);
        final List<Service> assignedServices = this.groupService.get(Long.parseLong(groupId))
                .getServices().stream().toList();

        possibleServices.removeAll(assignedServices);

        return ResponseEntity.ok().body(
                serviceRepository.findAll(
                    serviceRepository.filterBuilder()
                            .with(ServiceField.ID, ConditionType.IN, possibleServices)
                            .build()
            ));
    }


    @PutMapping("/{code}/services/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BRANCH_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity updateService(@PathVariable(name="code") final String code,
                                        @PathVariable(name = "id") final Long id,
                                        @RequestBody final ServiceRequestDto request) {
        if (!this.authService.canChangeTenant(code)) {
            return ResponseEntity.notFound().build();
        }

        try{
            return ResponseEntity.ok().body(tenantService.updateService(code, id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{code}/services/{id}")
    @PreAuthorize("hasAnyRole('ROLE_BRANCH_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity deleteService(@PathVariable(name="code") final String code, @PathVariable(name = "id") final Long id) {
        if (!this.authService.canChangeTenant(code)) {
            return ResponseEntity.notFound().build();
        }

        try{
            tenantService.deleteService(id);
            return ResponseEntity.ok().body(new SimpleMessageDto("Service with ID " + id + " deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
