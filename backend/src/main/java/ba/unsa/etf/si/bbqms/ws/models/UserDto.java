package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.User;

public record UserDto(long id, String email, String phoneNumber) {

    public static UserDto fromEntity(final User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getPhoneNumber());
    }
}
