package ba.unsa.etf.si.bbqms.ws.models;

public class TenantIdDto {
    private final Long tenantId;

    public TenantIdDto(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }


}
