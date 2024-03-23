package ba.unsa.etf.si.bbqms.ws.models;

public class AddTenantDto {

    private String code;
    private String name;
    private String hq_address;
    private String font;
    private String welcomeMessage;
    private String logo;
    private Long userId;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}