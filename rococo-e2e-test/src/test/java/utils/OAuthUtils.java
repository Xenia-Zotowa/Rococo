package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuthUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] codeVerifier = new byte[32];
        SECURE_RANDOM.nextBytes(codeVerifier);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(codeVerifier);
    }

    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}