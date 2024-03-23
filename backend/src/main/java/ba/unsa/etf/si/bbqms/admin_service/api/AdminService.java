package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.ws.models.AddTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.FindTenantDto;
import ba.unsa.etf.si.bbqms.ws.models.TenantIdDto;

public interface AdminService {
    public TenantIdDto addTenant(AddTenantDto request);
    public FindTenantDto getById(Long id);

}
