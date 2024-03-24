package ba.unsa.etf.si.bbqms.tenant_service.api;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;

public interface TenantService {
    public Tenant addTenant(final AddTenantDto request);
    public Tenant findByCode(final String code);
}
