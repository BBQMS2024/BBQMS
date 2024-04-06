package ba.unsa.etf.si.bbqms.tenant_service.api;

import ba.unsa.etf.si.bbqms.domain.Tenant;
import ba.unsa.etf.si.bbqms.ws.models.TenantDto;

public interface TenantService {
    Tenant addTenant(final TenantDto request);
    Tenant findByCode(final String code);
    Tenant updateTenant(final String code, final TenantDto request);
}
