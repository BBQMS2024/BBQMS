package ba.unsa.etf.si.bbqms.tenant_service.implementation;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantLogoRepository tenantLogoRepository;

    @Autowired
    public TenantServiceImpl(final TenantRepository tenantRepository, final TenantLogoRepository tenantLogoRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantLogoRepository = tenantLogoRepository;
    }
    @Override
    public Tenant addTenant(final AddTenantDto request){
        final Tenant tenant = new Tenant(
                request.getCode(),
                request.getName(),
                request.getHq_address(),
                request.getFont(),
                request.getWelcomeMessage()
        );

        final TenantLogo newLogo = new TenantLogo();
        newLogo.setBase64Logo(request.getLogo());
        final TenantLogo savedLogo = tenantLogoRepository.save(newLogo);
        tenant.setLogo(savedLogo);
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant findByCode(final String code) throws EntityNotFoundException {
        return tenantRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("No tenant found with code: " + code));
    }
}
