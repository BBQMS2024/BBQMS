package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {
    private final TenantService tenantService;

    public TenantController(final TenantService tenantService) {
        this.tenantService = tenantService;
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
    public ResponseEntity updateTenant(@PathVariable final String code, @RequestBody final TenantDto request) {
        try{
            return ResponseEntity.ok().body(tenantService.updateTenant(code, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
