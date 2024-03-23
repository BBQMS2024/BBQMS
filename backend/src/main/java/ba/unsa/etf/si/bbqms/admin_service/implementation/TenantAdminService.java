package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.domain.User;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.repository.UserRepository;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantIdDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantAdminService implements AdminService {

    private final TenantRepository tenantRepository;
    private final TenantLogoRepository tenantLogoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TenantAdminService(TenantRepository tenantRepository, TenantLogoRepository tenantLogoRepository,
                              UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantLogoRepository = tenantLogoRepository;
        this.userRepository = userRepository;
    }
    @Override
    public TenantIdDto saveTenant(AddTenantDto request) throws EntityNotFoundException{
        Tenant tenant = new Tenant();
        tenant.setCode(request.getCode());
        tenant.setName(request.getName());
        tenant.setHqAddress(request.getHq_address());
        tenant.setFont(request.getFont());
        tenant.setWelcomeMessage(request.getWelcomeMessage());

        Optional<User> user = userRepository.findById(request.getUserId());

        if (user.isPresent()){
            User u = user.get();

            TenantLogo newLogo = new TenantLogo();
            newLogo.setBase64Logo(request.getLogo());
            TenantLogo savedLogo = tenantLogoRepository.save(newLogo);

            tenant.setLogo(savedLogo);
            Tenant savedTenant = tenantRepository.save(tenant);
            u.setTenant(savedTenant);
            return new TenantIdDto(savedTenant.getId());
        }
        else{
            throw new EntityNotFoundException("No user with id : " + request.getUserId());
        }
    }
}
