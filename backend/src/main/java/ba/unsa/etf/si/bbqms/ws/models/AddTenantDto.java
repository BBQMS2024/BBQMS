package ba.unsa.etf.si.bbqms.ws.models;

public class AddTenantDto {
    private final String code;
    private final String name;
    private final String hqAddress;
    private final String font;
    private final String welcomeMessage;
    private final String logo;

    public AddTenantDto(final String code, final String name, final String hqAddress, final String font, final String welcomeMessage, final String logo) {
        this.code = code;
        this.name = name;
        this.hqAddress = hqAddress;
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

    public String getHqAddress() {
        return hqAddress;
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
