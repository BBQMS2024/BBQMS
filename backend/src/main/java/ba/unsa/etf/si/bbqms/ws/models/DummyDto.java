package ba.unsa.etf.si.bbqms.ws.models;


//These are our DTO's (Data-transfer-object) these will be what will be sent from controllers to our frontends
public class DummyDto {
    private final String text;

    public DummyDto(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
