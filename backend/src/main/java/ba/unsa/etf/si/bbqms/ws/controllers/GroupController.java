package ba.unsa.etf.si.bbqms.ws.controllers;

import ba.unsa.etf.si.bbqms.admin_service.api.BranchService;
import ba.unsa.etf.si.bbqms.auth_service.api.AuthService;
import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.ws.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    private final BranchService branchService;
    private final AuthService authService;

    public GroupController(final GroupService groupService,
                           final BranchService branchService,
                           final AuthService authService) {
        this.groupService = groupService;
        this.branchService = branchService;
        this.authService = authService;
    }

    @GetMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity getAll(@PathVariable final String tenantCode){
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final List<BranchGroup> branchGroupList = this.groupService.getAllByTenant(tenantCode);
            final List<BranchGroupResponseDto> branchGroupResponseDtoList = branchGroupList.stream()
                    .map(BranchGroupResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(branchGroupResponseDtoList);
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @GetMapping("/{code}/{groupId}/assignable/branch")
    public List<BranchDto> findAssignableBranches(@PathVariable final String code, @PathVariable final String groupId) {
        return this.groupService.findAssignableBranches(Long.parseLong(groupId), code).stream()
                .map(BranchDto::fromEntity)
                .toList();
    }

    @GetMapping("/{code}/{groupId}/assignable/service")
    public List<ServiceDto> findAssignableServices(@PathVariable final String code, @PathVariable final String groupId) {
        return this.groupService.findAssignableServices(Long.parseLong(groupId), code).stream()
                .map(ServiceDto::fromEntity)
                .toList();
    }

    @PostMapping("/{tenantCode}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroup(@PathVariable final String tenantCode, @RequestBody final BranchGroupCreateDto request) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup addedBranchGroup = this.groupService.addBranchGroup(tenantCode, request);
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(addedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{groupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupService(@PathVariable final String tenantCode,
                                                @PathVariable final String groupId,
                                                @PathVariable final String serviceId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup updatedBranchGroup = this.groupService.addBranchGroupService(Long.parseLong(groupId), Long.parseLong(serviceId));
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(updatedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{groupId}/branches/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity addBranchGroupBranch(@PathVariable final String tenantCode,
                                               @PathVariable final String groupId,
                                               @PathVariable final String branchId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup updatedBranchGroup = this.groupService.addBranchGroupBranch(Long.parseLong(groupId), Long.parseLong(branchId));
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(updatedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @PutMapping("/{tenantCode}/{branchGroupId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity updateBranchGroup(@PathVariable final String tenantCode,
                                            @PathVariable final String branchGroupId,
                                            @RequestBody final BranchGroupUpdateDto name) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup updatedBranchGroup = this.groupService.updateBranchGroup(Long.parseLong(branchGroupId), name);
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(updatedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{branchGroupId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroup(@PathVariable final String tenantCode, @PathVariable final String branchGroupId) {
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        this.groupService.deleteBranchGroup(Long.parseLong(branchGroupId));
        return ResponseEntity.ok().body(new SimpleMessageDto("Deleted branch group with id: " + branchGroupId));
    }

    @DeleteMapping("/{tenantCode}/{branchGroupId}/branches/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupBranch(@PathVariable final String tenantCode,
                                                  @PathVariable final String branchGroupId,
                                                  @PathVariable final String branchId){
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup updatedBranchGroup = this.groupService.deleteBranchGroupBranch(Long.parseLong(branchGroupId),Long.parseLong(branchId));
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(updatedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    @DeleteMapping("/{tenantCode}/{branchGroupId}/services/{serviceId}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_BRANCH_ADMIN')")
    public ResponseEntity deleteBranchGroupService(@PathVariable final String tenantCode,
                                                   @PathVariable final String branchGroupId,
                                                   @PathVariable final String serviceId){
        if (!this.authService.canChangeTenant(tenantCode)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final BranchGroup updatedBranchGroup = this.groupService.deleteBranchGroupService(Long.parseLong(branchGroupId),Long.parseLong(serviceId));
            return ResponseEntity.ok().body(BranchGroupResponseDto.fromEntity(updatedBranchGroup));
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
        }
    }

    public record BranchGroupResponseDto (Long id, String name, Set<ServiceResponseDto> services, Set<BranchDto> branches) {
        public static BranchGroupResponseDto fromEntity(final BranchGroup branchGroup) {
            final Set<Branch> branchSet = branchGroup.getBranches();
            final Set<Service> serviceSet = branchGroup.getServices();
            return new BranchGroupResponseDto(
                    branchGroup.getId(),
                    branchGroup.getName(),
                    serviceSet.stream().map(ServiceResponseDto::fromEntity).collect(Collectors.toSet()),
                    branchSet.stream().map(BranchDto::fromEntity).collect(Collectors.toSet())
            );
        }
    }
}
