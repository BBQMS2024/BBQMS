package ba.unsa.etf.si.bbqms.ws.models;

public class TenantIdDto {
    private Long tenantId;

    public TenantIdDto() {
    }

    public TenantIdDto(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}
