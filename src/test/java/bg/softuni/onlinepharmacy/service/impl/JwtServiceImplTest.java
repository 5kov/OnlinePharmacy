package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    private final String secret = "testSecretKeyForJwtTokenThatIsVerySecure!";
    private final long expiration = 3600000L; // 1 hour in milliseconds

    @BeforeEach
    void setUp() {
        jwtService = spy(new JwtServiceImpl(secret, expiration));
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        // Arrange
        String userId = "12345";
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        // Act
        String token = jwtService.generateToken(userId, claims);

        // Assert
        String extractedUserId = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals(userId, extractedUserId);

        String extractedRole = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        assertEquals("USER", extractedRole);

        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        Date now = new Date();
        assertEquals(expirationDate.getTime(), now.getTime() + expiration, 1000);
    }

    @Test
    void getSigningKey_ShouldReturnCorrectKey() {
        // Arrange
        var expectedKeyBytes = secret.getBytes();

        // Act
        var signingKey = jwtService.getSigningKey();

        // Assert
        assertEquals(signingKey.getAlgorithm(), "HmacSHA256");
        assertEquals(signingKey.getEncoded().length, expectedKeyBytes.length);
    }
}

