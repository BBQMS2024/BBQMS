package ba.unsa.etf.si.bbqms.tenant_service.api;

import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.ws.models.ServiceDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;

import java.util.List;

public interface TenantService {
    Tenant addTenant(final TenantDto request);
    Tenant findByCode(final String code);
    Tenant updateTenant(final String code, final TenantDto request);
    Service getServiceById(final long id);
    List<Service> getAllServicesByTenant(final String Code) throws Exception;
    Service addService(final String code, final ServiceDto request) throws Exception;
    Service updateService(final String Code, final long id, final ServiceDto request) throws Exception;
    void deleteService(final long id);
}
