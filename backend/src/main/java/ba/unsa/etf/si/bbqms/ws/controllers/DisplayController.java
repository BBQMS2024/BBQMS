package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.DisplayService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Display;
import ba.unsa.etf.si.bbqms.ws.models.DisplayDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/displays")
public class DisplayController {
    private final DisplayService displayService;
    private final AuthService authService;

    public DisplayController(DisplayService displayService, AuthService authService) {
        this.displayService = displayService;
        this.authService = authService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity createDisplay(@RequestBody final DisplayRequest displayRequest){
        try{
            final Display createdDisplay = this.displayService.createDisplay(displayRequest.name(),
                    displayRequest.tellerStationId());
            return ResponseEntity.ok().body(DisplayDto.fromEntity(createdDisplay));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    public record DisplayRequest(String name, Long tellerStationId){
    }
}
