package ba.unsa.etf.si.bbqms.ws.models;

<<<<<<< HEAD

public record ServiceDto (String name){
=======
import ba.unsa.etf.si.bbqms.domain.Service;

public record ServiceDto(long id, String name) {
    public static ServiceDto fromEntity(final Service service) {
        return new ServiceDto(service.getId(), service.getName());
    }
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
