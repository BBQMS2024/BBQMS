package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.queue_service.api.QueueService;
import ba.unsa.etf.si.bbqms.ws.models.DummyDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public DummyDto example() {
        String example = adminService.dummyAdminServiceImpl();
        return new DummyDto(example);
    }
}
