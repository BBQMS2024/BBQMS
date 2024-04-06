package ba.unsa.etf.si.bbqms.ws.models;

import ba.unsa.etf.si.bbqms.domain.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDto(long id, String email, String phoneNumber, Set<String> roles) {
    public static UserDto fromEntity(final User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }
}
