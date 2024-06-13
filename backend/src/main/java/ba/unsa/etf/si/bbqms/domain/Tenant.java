package ba.unsa.etf.si.bbqms.domain;

import ba.ekapic1.stonebase.model.Model;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tenant")
@Model
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "hq_address")
    private String hqAddress;

    @Column(name = "font")
    private String font;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    @OneToOne
    @JoinColumn(name = "logo_id", referencedColumnName = "id")
    private TenantLogo logo;

    @OneToMany(mappedBy = "tenant")
    private Set<Service> service;

    @OneToMany(mappedBy = "tenant")
    private Set<Branch> branches;

    public Tenant() {
    }

    public Tenant(final String code,
                  final String name,
                  final String hqAddress,
                  final String font,
                  final String welcomeMessage,
                  final TenantLogo logo) {
        this.code = code;
        this.name = name;
        this.hqAddress = hqAddress;
        this.font = font;
        this.welcomeMessage = welcomeMessage;
        this.logo = logo;
    }

    public Tenant(final String code,
                  final String name,
                  final String hqAddress,
                  final String font,
                  final String welcomeMessage) {
        this(code, name, hqAddress, font, welcomeMessage, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getHqAddress() {
        return hqAddress;
    }

    public void setHqAddress(final String hqAddress) {
        this.hqAddress = hqAddress;
    }

    public String getFont() {
        return font;
    }

    public void setFont(final String font) {
        this.font = font;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(final String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public TenantLogo getLogo() {
        return logo;
    }

    public void setLogo(final TenantLogo logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", hq_address='" + hqAddress + '\'' +
                ", font='" + font + '\'' +
                ", welcomeMessage='" + welcomeMessage + '\'' +
                ", logo=" + logo +
                '}';
    }
}
