package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/tenants")
    public ResponseEntity addTenant(@RequestBody AddTenantDto tenantDto) {
        return ResponseEntity.ok().body(adminService.addTenant(tenantDto));
    }

    @GetMapping("/tenants/{id}")
    public ResponseEntity getTenant(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok().body(adminService.getById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

}
