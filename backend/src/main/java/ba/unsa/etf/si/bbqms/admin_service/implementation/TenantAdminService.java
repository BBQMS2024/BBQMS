package ba.unsa.etf.si.bbqms.admin_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.AdminService;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.FindTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantIdDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantAdminService implements AdminService {

    private final TenantRepository tenantRepository;
    private final TenantLogoRepository tenantLogoRepository;

    @Autowired
    public TenantAdminService(TenantRepository tenantRepository, TenantLogoRepository tenantLogoRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantLogoRepository = tenantLogoRepository;
    }
    @Override
    public TenantIdDto addTenant(AddTenantDto request){
        Tenant tenant = new Tenant();
        tenant.setCode(request.getCode());
        tenant.setName(request.getName());
        tenant.setHqAddress(request.getHq_address());
        tenant.setFont(request.getFont());
        tenant.setWelcomeMessage(request.getWelcomeMessage());

        TenantLogo newLogo = new TenantLogo();
        newLogo.setBase64Logo(request.getLogo());
        TenantLogo savedLogo = tenantLogoRepository.save(newLogo);

        tenant.setLogo(savedLogo);
        Tenant savedTenant = tenantRepository.save(tenant);
        return new TenantIdDto(savedTenant.getId());

    }

    @Override
    public FindTenantDto getById(Long id) throws EntityNotFoundException{
        Optional<Tenant> tenant = tenantRepository.findById(id);
        if (tenant.isPresent()){
            Tenant foundTenant = tenant.get();
            return new FindTenantDto(
                    foundTenant.getLogo().getBase64Logo(),
                    foundTenant.getWelcomeMessage(),
                    foundTenant.getFont()
            );
        }
        else {
            throw new EntityNotFoundException("No tenant found with id: " + id);
        }


    }
}
