package deti.tqs.cinemax.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import deti.tqs.cinemax.exceptions.SecretKeyGenerationException;

@Service
public class JwtUtilService {

    private final SecretKey secretKey;

    public JwtUtilService() throws SecretKeyGenerationException {
        // Generate a secret key with the desired algorithm (HS256)
        this.secretKey = generateSecretKey();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> extraClaims, String user) {
        return Jwts.builder().claims().empty().add(extraClaims).subject(user)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).and()
                .signWith(secretKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey generateSecretKey() throws SecretKeyGenerationException {
        // Define the key byte size
        int keyByteSize = 32; // Example: 32 bytes for HS256 algorithm

        // Generate random bytes
        byte[] keyBytes = new byte[keyByteSize];
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SecretKeyGenerationException("Failed to generate secret key", e);
        }

        // Return the secret key using the generated bytes and the algorithm
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}