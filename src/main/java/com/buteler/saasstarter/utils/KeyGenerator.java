package com.buteler.saasstarter.utils;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * This class generates a random key to be used as a secret key for JWT token generation.
 */
public class KeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS512.key().build(); // Generate a secure random key
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Key: " + base64Key);
    }

}

