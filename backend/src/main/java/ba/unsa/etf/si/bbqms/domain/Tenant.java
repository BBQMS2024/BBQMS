package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant")
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
    private String hq_address;

    @Column(name = "font")
    private String font;

    @Column(name = "welcome_message")
    private String welcomeMessage;

    @OneToOne()
    @JoinColumn(name = "logo_id", referencedColumnName = "id")
    private TenantLogo logo;

    @OneToOne(mappedBy = "tenant")
    private User user;

    public Tenant() {
    }

    public Tenant(String code, String name, String hq_address, String font, String welcomeMessage, TenantLogo logo, User user) {
        this.code = code;
        this.name = name;
        this.hq_address = hq_address;
        this.font = font;
        this.welcomeMessage = welcomeMessage;
        this.logo = logo;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHq_address() {
        return hq_address;
    }

    public void setHq_address(String hq_address) {
        this.hq_address = hq_address;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public TenantLogo getLogo() {
        return logo;
    }

    public void setLogo(TenantLogo logo) {
        this.logo = logo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", hq_address='" + hq_address + '\'' +
                ", font='" + font + '\'' +
                ", welcomeMessage='" + welcomeMessage + '\'' +
                ", logo=" + logo +
                ", user=" + user +
                '}';
    }

}
