package ba.unsa.etf.si.bbqms.tenant_service.implementation;

import ba.unsa.etf.si.bbqms.admin_service.api.GroupService;
import ba.unsa.etf.si.bbqms.domain.BranchGroup;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.domain.TenantLogo;
import ba.unsa.etf.si.bbqms.repository.ServiceRepository;
import ba.unsa.etf.si.bbqms.repository.TenantLogoRepository;
import ba.unsa.etf.si.bbqms.repository.TenantRepository;
import ba.unsa.etf.si.bbqms.tenant_service.api.TenantService;
import ba.unsa.etf.si.bbqms.ws.models.ServiceRequestDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@org.springframework.stereotype.Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final TenantLogoRepository tenantLogoRepository;
    private final ServiceRepository serviceRepository;
    private final GroupService groupService;

    @Autowired
    public TenantServiceImpl(final TenantRepository tenantRepository,
                             final TenantLogoRepository tenantLogoRepository,
                             final ServiceRepository serviceRepository,
                             final GroupService groupService) {
        this.tenantRepository = tenantRepository;
        this.tenantLogoRepository = tenantLogoRepository;
        this.serviceRepository = serviceRepository;
        this.groupService = groupService;
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
    public Service getServiceById(final long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No service found with id: " + id));
    }

    @Override
    public List<Service> getAllServicesByTenant(final String code) {
        return serviceRepository.findAllByTenant_Code(code);
    }

    @Override
    public Service addService(final String code, final ServiceRequestDto request) throws Exception {
        final Tenant tenant= tenantRepository.findByCode(code).
                orElseThrow(() -> new Exception("Tenant not found") );

        final List<Service> services = serviceRepository.findAllByTenant_Code(code);
        final boolean nameExists = services.stream()
                .anyMatch(service -> service.getName().equals(request.name()));
        if(nameExists){
            throw new Exception("Name already in use!");
        }

        final Service service = new Service(
                request.name(),
                tenant
        );
        return serviceRepository.save(service);
    }

    @Override
    public Service updateService(final String code, final long id, final ServiceRequestDto request) throws Exception {
        final List<Service> services = serviceRepository.findAllByTenant_Code(code);
        final boolean nameExists = services.stream()
                .anyMatch(service -> service.getName().equals(request.name()));
        if(nameExists){
            throw new Exception("Name already in use!");
        }
        final Service service = getServiceById(id);
        if (request.name() != null) {
            service.setName(request.name());
        }
        return serviceRepository.save(service);
    }

    @Override
    public void deleteService(final long id) {
        // If we're deleting a service we first must discard it from groups/teller stations that are already using it
        final Service service = this.serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No service with id: " + id));

        for (final BranchGroup group : this.groupService.getAllOfferingService(service)) {
            this.groupService.deleteBranchGroupService(group.getId(), service.getId());
        }

        serviceRepository.deleteById(id);
    }
}
