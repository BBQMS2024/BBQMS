package ba.unsa.etf.si.bbqms.auth_service.implementation;

import ba.unsa.etf.si.bbqms.auth_service.api.RoleService;
import ba.unsa.etf.si.bbqms.domain.Role;
import ba.unsa.etf.si.bbqms.domain.RoleName;
import ba.unsa.etf.si.bbqms.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultRoleService implements RoleService {
    private final RoleRepository roleRepository;

    public DefaultRoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> getRoleByName(final RoleName roleName) {
        return this.roleRepository.findByName(roleName);
    }
}
