package ba.unsa.etf.si.bbqms.tfa_service.api;

import java.net.UnknownHostException;
import java.util.Optional;

public interface TwoFactorService {
    String generateNewSecret();
    Optional<String> generateQrCodeUri(final String secret);
    boolean isCodeValid(final String code, final String secret) throws UnknownHostException;
}
