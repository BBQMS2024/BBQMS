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

    @PostMapping("/addTenant")
    public ResponseEntity addTenant(@RequestBody AddTenantDto tenantDto) {
        try {
            return ResponseEntity.ok().body(adminService.saveTenant(tenantDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

}
