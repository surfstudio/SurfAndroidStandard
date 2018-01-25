package ru.surfstudio.android.filestorage.naming;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.surfstudio.android.core.app.log.Logger;

public class Sha256NamingProcessor implements NamingProcessor {

    private static final String ALGORITHM = "SHA-256";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HEX_FORMAT = "%064x";

    @Override
    public String getNameFrom(@NotNull String rawName) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
            md.update(rawName.getBytes(CHARSET_NAME));
            byte[] digest = md.digest();
            return String.format(HEX_FORMAT, new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return "";
    }
}
