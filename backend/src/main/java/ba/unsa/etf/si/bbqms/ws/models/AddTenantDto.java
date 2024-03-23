package ba.unsa.etf.si.bbqms.ws.models;

// note the userId field which I suspect will be necessary in the future
public class AddTenantDto {

    private final String code;
    private final String name;
    private final String hq_address;
    private final String font;
    private final String welcomeMessage;
    private final String logo;
    private final Long userId;

    public AddTenantDto(String code, String name, String hqAddress, String font, String welcomeMessage, String logo, Long userId) {
        this.code = code;
        this.name = name;
        hq_address = hqAddress;
        this.font = font;
        this.welcomeMessage = welcomeMessage;
        this.logo = logo;
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getHq_address() {
        return hq_address;
    }

    public String getFont() {
        return font;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getLogo() {
        return logo;
    }

    public Long getUserId() {
        return userId;
    }
}