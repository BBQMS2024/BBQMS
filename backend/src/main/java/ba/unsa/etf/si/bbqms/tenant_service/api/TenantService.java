package ba.unsa.etf.si.bbqms.tenant_service.api;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;

public interface TenantService {
    Tenant addTenant(final AddTenantDto request);
    Tenant findByCode(final String code);
}
