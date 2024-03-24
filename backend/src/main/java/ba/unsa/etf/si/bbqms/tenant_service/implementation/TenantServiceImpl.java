package ba.unsa.etf.si.bbqms.tenant_service.implementation;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
    public Tenant addTenant(final TenantDto request){
        final Tenant tenant = new Tenant(
                request.code(),
                request.name(),
                request.hqAddress(),
                request.font(),
                request.welcomeMessage()
        );

        final TenantLogo newLogo = new TenantLogo(request.logo());
        final TenantLogo savedLogo = tenantLogoRepository.save(newLogo);
        tenant.setLogo(savedLogo);
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant findByCode(final String code) throws EntityNotFoundException {
        return tenantRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("No tenant found with code: " + code));
    }

    @Override
    public Tenant updateTenant(final String code, final TenantDto request) throws EntityNotFoundException{
        Tenant tenant = findByCode(code);

        tenant.setName(request.name());
        tenant.setCode(request.code());
        tenant.setHqAddress(request.hqAddress());
        tenant.setFont(request.font());
        tenant.setWelcomeMessage(request.welcomeMessage());

        if(request.logo() != null)
           tenant.getLogo().setBase64Logo(request.logo());
        Optional<Tenant> optionalTenant = tenantRepository.findByCode(request.code());
        if (optionalTenant.isPresent() && !Objects.equals(code, request.code()))
            throw new EntityExistsException("Tenant with code " + request.code() + " already exists");
        return tenantRepository.save(tenant);
    }
}
