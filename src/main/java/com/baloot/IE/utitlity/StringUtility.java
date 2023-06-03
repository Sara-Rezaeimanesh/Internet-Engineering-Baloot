package com.baloot.IE.utitlity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringUtility {
    public static String quoteWrapper(String input){
        return "\"" + input + "\"";
    }

    public static String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        assert digest != null;
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
