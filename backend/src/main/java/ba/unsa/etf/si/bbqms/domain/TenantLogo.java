package ba.unsa.etf.si.bbqms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class TenantLogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "base64_logo")
    private String base64Logo;

    @OneToOne(mappedBy = "logo")
    @JsonIgnore
    private Tenant tenant;

    public TenantLogo() {
    }

    public TenantLogo(final String base64Logo, final Tenant tenant) {
        this.base64Logo = base64Logo;
        this.tenant = tenant;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getBase64Logo() {
        return base64Logo;
    }

    public void setBase64Logo(final String base64Logo) {
        this.base64Logo = base64Logo;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(final Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return "TenantLogo{" +
                "id=" + id +
                ", base64Logo='" + base64Logo + '\'' +
                ", tenant=" + tenant +
                '}';
    }
}
