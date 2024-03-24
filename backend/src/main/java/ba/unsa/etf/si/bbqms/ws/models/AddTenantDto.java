package ba.unsa.etf.si.bbqms.ws.models;

public class AddTenantDto {
    private final String code;
    private final String name;
    private final String hq_address;
    private final String font;
    private final String welcomeMessage;
    private final String logo;

    public AddTenantDto(String code, String name, String hqAddress, String font, String welcomeMessage, String logo) {
        this.code = code;
        this.name = name;
        hq_address = hqAddress;
        this.font = font;
        this.welcomeMessage = welcomeMessage;
        this.logo = logo;
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
}
