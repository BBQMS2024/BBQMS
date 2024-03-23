package ba.unsa.etf.si.bbqms.ws.models;

public class FindTenantDto {
    private final String logo;
    private final String welcomeMessage;
    private final String welcomeMessageFont;

    public FindTenantDto(String logo, String welcomeMessage, String welcomeMessageFont) {
        this.logo = logo;
        this.welcomeMessage = welcomeMessage;
        this.welcomeMessageFont = welcomeMessageFont;
    }

    public String getLogo() {
        return logo;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getWelcomeMessageFont() {
        return welcomeMessageFont;
    }
}
