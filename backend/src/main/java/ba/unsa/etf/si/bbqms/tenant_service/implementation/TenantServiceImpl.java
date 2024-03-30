package ba.unsa.etf.si.bbqms.tenant_service.implementation;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.ServiceDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantLogoRepository tenantLogoRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public TenantServiceImpl(final TenantRepository tenantRepository,
                             final TenantLogoRepository tenantLogoRepository,
                             final ServiceRepository serviceRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantLogoRepository = tenantLogoRepository;
        this.serviceRepository = serviceRepository;
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
        final Tenant tenant = findByCode(code);

        if (request.name() != null) {
            tenant.setName(request.name());
        }
        if (request.hqAddress() != null) {
            tenant.setHqAddress(request.hqAddress());
        }
        if (request.font() != null) {
            tenant.setFont(request.font());
        }
        if (request.welcomeMessage() != null) {
            tenant.setWelcomeMessage(request.welcomeMessage());
        }
        if(request.logo() != null) {
            tenant.getLogo().setBase64Logo(request.logo());
        }
        return tenantRepository.save(tenant);
    }

    @Override
    public ba.unsa.etf.si.bbqms.domain.Service getServiceById(long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + id));
    }

    @Override
    public List<ba.unsa.etf.si.bbqms.domain.Service> getAllServicesByTenant(Tenant tenant) {
        return serviceRepository.findByTenant(tenant);
    }

    @Override
    public ba.unsa.etf.si.bbqms.domain.Service addService(final ServiceDto request) {
        final ba.unsa.etf.si.bbqms.domain.Service service = new ba.unsa.etf.si.bbqms.domain.Service(
                request.name(),
                request.tenant()
        );
        return serviceRepository.save(service);
    }

    @Override
    public ba.unsa.etf.si.bbqms.domain.Service updateService(final long id, final ServiceDto request) {
        final ba.unsa.etf.si.bbqms.domain.Service service = getServiceById(id);
        if (request.name() != null) {
            service.setName(request.name());
        }
        if (request.tenant() != null) {
            service.setTenant(request.tenant());
        }
        return serviceRepository.save(service);
    }

    @Override
    public void deleteService(final long id) {
        serviceRepository.deleteById(id);
    }
}
