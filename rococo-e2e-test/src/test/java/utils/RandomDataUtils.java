package utils;

import java.util.UUID;

public class RandomDataUtils {

    public static String randomUsername() {
        return "test_user_" + UUID.randomUUID().toString().substring(0, 8);
    }
}