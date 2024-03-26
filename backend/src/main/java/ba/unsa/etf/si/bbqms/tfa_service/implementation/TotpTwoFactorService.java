package ba.unsa.etf.si.bbqms.tfa_service.implementation;

import ba.unsa.etf.si.bbqms.tfa_service.api.TwoFactorService;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TotpTwoFactorService implements TwoFactorService {
    private static final Logger logger = LoggerFactory.getLogger(TotpTwoFactorService.class);

    @Value("${tfa.label}")
    public String TFA_LABEL;

    @Value("${tfa.issuer}")
    public String TFA_ISSUER;


    @Override
    public String generateNewSecret() {
        return new DefaultSecretGenerator().generate();
    }

    @Override
    public Optional<String> generateQrCodeUri(final String secret) {
        final QrData qrData = new QrData.Builder()
                .label(this.TFA_LABEL)
                .issuer(this.TFA_ISSUER)
                .secret(secret)
                .build();
        try {
            QrGenerator qrGenerator = new ZxingPngQrGenerator();
            return Optional.of(Utils.getDataUriForImage(qrGenerator.generate(qrData), qrGenerator.getImageMimeType()));
        } catch (QrGenerationException e) {
            logger.error("Error while trying trying to generate QR-code.");
            return Optional.empty();
        }
    }

    @Override
    public boolean isCodeValid(final String code, final String secret) {
        DefaultCodeVerifier codeVerifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());
        return codeVerifier.isValidCode(secret, code);
    }
}
