package ba.unsa.etf.si.bbqms.tenant_service.api;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Tenant;
<<<<<<< HEAD
import ba.unsa.etf.si.bbqms.ws.models.ServiceDto;
=======
import ba.unsa.etf.si.bbqms.ws.models.ServiceRequestDto;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;

import java.util.List;

public interface TenantService {
    Tenant addTenant(final TenantDto request);
    Tenant findByCode(final String code);
    Tenant updateTenant(final String code, final TenantDto request);
    Service getServiceById(final long id);
    List<Service> getAllServicesByTenant(final String Code) throws Exception;
<<<<<<< HEAD
    Service addService(final String code, final ServiceDto request) throws Exception;
    Service updateService(final String Code, final long id, final ServiceDto request) throws Exception;
=======
    Service addService(final String code, final ServiceRequestDto request) throws Exception;
    Service updateService(final String Code, final long id, final ServiceRequestDto request) throws Exception;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    void deleteService(final long id);
}
