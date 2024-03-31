package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
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

    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getAll(){
        try {
            List<BranchGroup> branchGroupList = groupService.getAll();
            List<BranchGroupResponseDto> branchGroupResponseDtoList = branchGroupList.stream()
                    .map(BranchGroupResponseDto::fromEntity).collect(Collectors.toList());
            return ResponseEntity.ok().body(branchGroupResponseDtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroup(@RequestBody final BranchGroupCreateDto request) {
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroup(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{groupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupService(@PathVariable final String groupId, @PathVariable final String serviceId) {
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroupService(Long.parseLong(groupId), Long.parseLong(serviceId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{groupId}/branches/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupBranch(@PathVariable final String groupId, @PathVariable final String branchId) {
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.addBranchGroupBranch(Long.parseLong(groupId), Long.parseLong(branchId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateBranchGroup(@PathVariable final String id, @RequestBody final BranchGroupUpdateDto name) {
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.updateBranchGroup(Long.parseLong(id), name)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroup(@PathVariable final String id) {
        try {
            groupService.deleteBranchGroup(Long.parseLong(id));
            return ResponseEntity.ok().body(new SimpleMessageDto("Deleted branch group with id: " + id));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{groupId}/branches/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupBranch(@PathVariable final String groupId, @PathVariable final String branchId){
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.deleteBranchGroupBranch(Long.parseLong(groupId),Long.parseLong(branchId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{groupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupService(@PathVariable final String groupId, @PathVariable final String serviceId){
        try {
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(groupService.deleteBranchGroupService(Long.parseLong(groupId),Long.parseLong(serviceId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
