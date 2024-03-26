package ba.unsa.etf.si.bbqms.utils;

import ba.unsa.etf.si.bbqms.domain.User;

public class UserValidator {
    public static boolean validateData(final User user) {
        return user.getEmail() != null &&
                EmailValidator.validate(user.getEmail().trim()) &&
                user.getPassword() != null &&
                user.getPassword().trim().length() >= 4;
    }
}
