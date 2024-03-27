package ba.unsa.etf.si.bbqms;

import ba.unsa.etf.si.bbqms.tfa_service.implementation.TotpTwoFactorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TfaServiceTests {
    @InjectMocks
    TotpTwoFactorService totpTwoFactorService;

    @Test
    public void testGenerateQrCodeUri_ShouldReturnQRCode() {
        final String secret = "secret";

        Optional<String> optionalString = totpTwoFactorService.generateQrCodeUri(secret);

        assertTrue(optionalString.isPresent());
    }
}
