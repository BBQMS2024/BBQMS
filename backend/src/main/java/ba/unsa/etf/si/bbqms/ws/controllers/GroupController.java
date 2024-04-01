package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.ws.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    private final AuthService authService;

    public GroupController(final GroupService groupService, AuthService authService) {
        this.groupService = groupService;
        this.authService = authService;
    }

    @GetMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getAll(@PathVariable final String tenantCode){
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<BranchGroup> branchGroupList = groupService.getAll();
            List<BranchGroupResponseDto> branchGroupResponseDtoList = branchGroupList.stream()
                    .map(BranchGroupResponseDto::fromEntity).collect(Collectors.toList());
            return ResponseEntity.ok().body(branchGroupResponseDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PostMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroup(@PathVariable final String tenantCode, @RequestBody final BranchGroupCreateDto request) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroup(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{groupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupService(@PathVariable final String tenantCode, @PathVariable final String groupId, @PathVariable final String serviceId) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroupService(Long.parseLong(groupId), Long.parseLong(serviceId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{groupId}/branches/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupBranch(@PathVariable final String tenantCode, @PathVariable final String groupId, @PathVariable final String branchId) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroupBranch(Long.parseLong(groupId), Long.parseLong(branchId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{branchGroupId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateBranchGroup(@PathVariable final String tenantCode, @PathVariable final String branchGroupId, @RequestBody final BranchGroupUpdateDto name) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.updateBranchGroup(Long.parseLong(branchGroupId), name)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{branchGroupId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroup(@PathVariable final String tenantCode, @PathVariable final String branchGroupId) {
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            groupService.deleteBranchGroup(Long.parseLong(branchGroupId));
            return ResponseEntity.ok().body(new SimpleMessageDto("Deleted branch group with id: " + branchGroupId));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{branchId}/branches/{branchGroupId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupBranch(@PathVariable final String tenantCode, @PathVariable final String branchGroupId, @PathVariable final String branchId){
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.deleteBranchGroupBranch(Long.parseLong(branchGroupId),Long.parseLong(branchId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{branchGroupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupService(@PathVariable final String tenantCode, @PathVariable final String branchGroupId, @PathVariable final String serviceId){
        final User currentUser = authService.getAuthenticatedUser();

        if(!currentUser.getTenant().getCode().equals(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.deleteBranchGroupService(Long.parseLong(branchGroupId),Long.parseLong(serviceId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
