package com.example.kyscanner.security;
import android.widget.Toast;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JWTUtil {

    // Replace this with your actual secret key (must be at least 32 characters for HS256)
    // **Use the same secret key used in Go**
    private static final String SECRET_KEY = "meowmeowmeowmeowmeowmeowmeowmeow69";  // Replace with actual key

    // Method to get the signing key
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Decode the JWT token
    public static String decodeJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())  // Use the same secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("gmail", String.class);  // Extract "gmail" claim
        } catch (Exception e) {
             return " Invalid or Expired Token ";
        }

    }
    // Generate a JWT for Testing (Optional)
}