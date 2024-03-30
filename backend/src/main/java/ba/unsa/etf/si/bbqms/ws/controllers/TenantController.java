package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.ServiceDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private final TenantService tenantService;
    private final AuthService authService;

    public TenantController(final TenantService tenantService, final AuthService authService) {
        this.tenantService = tenantService;
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
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity updateTenant(@PathVariable final String code, @RequestBody final TenantDto request) throws Exception {
        final User user = this.authService.getCurrentUser()
                .orElseThrow(() -> new Exception("We should be authenticated here hence have a current user. This exception should never happen."));

        if (!user.getTenant().getCode().equals(code)) {
            return ResponseEntity.notFound().build();
            // User tried to edit a tenant of whom he isn't part of. Not allowed.
        }
        
        try{
            return ResponseEntity.ok().body(tenantService.updateTenant(code, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PostMapping("/{code}/services")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity addService(@PathVariable(name = "code") final String code, @RequestBody final ServiceDto serviceDto) throws Exception {
        final User user = this.authService.getCurrentUser()
                .orElseThrow(() -> new Exception("We should be authenticated here hence have a current user. This exception should never happen."));

        boolean hasBranchAdminRole = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_BRANCH_ADMIN"));

        if (!hasBranchAdminRole) {
            throw new Exception("User does not have necessary role");
        }

        return ResponseEntity.ok().body(tenantService.addService(serviceDto));
    }

    @GetMapping("/{code}/services")
    public ResponseEntity getAllServicesByTenant(@PathVariable(name = "code") final String code) {
        try {
            return ResponseEntity.ok().body(tenantService.getAllServicesByTenant(tenantService.findByCode(code)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{code}/services/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity updateService(@PathVariable(name="code") final String code, @PathVariable(name = "id") final Long id, @RequestBody final ServiceDto request) throws Exception {
        final User user = this.authService.getCurrentUser()
                .orElseThrow(() -> new Exception("We should be authenticated here hence have a current user. This exception should never happen."));

        boolean hasBranchAdminRole = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_BRANCH_ADMIN"));

        if (!hasBranchAdminRole) {
            throw new Exception("User does not have necessary role");
        }

        if (!user.getTenant().getCode().equals(code)) {
            return ResponseEntity.notFound().build();
        }
        try{
            return ResponseEntity.ok().body(tenantService.updateService(id, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{code}/services/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteService(@PathVariable(name="code") final String code, @PathVariable(name = "id") final Long id) throws Exception {
        final User user = this.authService.getCurrentUser()
                .orElseThrow(() -> new Exception("We should be authenticated here hence have a current user. This exception should never happen."));
        boolean hasBranchAdminRole = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_BRANCH_ADMIN"));

        if (!hasBranchAdminRole) {
            throw new Exception("User does not have necessary role");
        }
        if (!user.getTenant().getCode().equals(code)) {
            return ResponseEntity.notFound().build();
        }
        try{
            tenantService.deleteService(id);
            return ResponseEntity.ok().body("Service with ID " + id + " deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
