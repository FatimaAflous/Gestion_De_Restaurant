package com.projet_restaurant.serviceutilisateurs.Controller;
import com.projet_restaurant.serviceutilisateurs.Dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Oauth2_controller {

    private AuthenticationManager authenticationManager;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;
    private UserDetailsService userDetailsService;

    public Oauth2_controller(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println(email + " and " + password);

        // Vérifier l'authentification
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Générer les tokens
        Instant now = Instant.now();

        // Récupérer les autorités de l'utilisateur
        String scope = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName()) // email
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .claim("email", authenticate.getName()) // Ajout de l'email
                .claim("scope", scope)
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName()) // email
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .build();
        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();
        System.out.println("User authenticated: " + authenticate.getName() + " with role(s): " + scope);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("Access_Token", accessToken);
        tokens.put("Refresh_Token", refreshToken);

        return tokens;
    }

    @PostMapping("/refresh")
public Map<String, String> refreshToken(@RequestParam String refreshToken) {
    // Vérifier si le Refresh Token est fourni
    if (refreshToken == null || refreshToken.isBlank()) {
        throw new IllegalArgumentException("Le Refresh Token est nécessaire.");
    }

    // Décoder le Refresh Token
    Jwt decodedToken;
    try {
        decodedToken = jwtDecoder.decode(refreshToken);
    } catch (Exception e) {
        throw new IllegalArgumentException("Le Refresh Token est invalide ou expiré.");
    }

    // Extraire les informations utilisateur du Refresh Token
    String email = decodedToken.getSubject(); // Utilise le champ `subject` pour l'email
    String scope = decodedToken.getClaimAsString("scope"); // Récupérer les scopes depuis les claims

    // Récupérer les détails utilisateur pour validation
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    // Générer un nouveau Access Token
    Instant now = Instant.now();
    JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
            .issuer("MS_sec")
            .subject(email) // Email comme sujet
            .issuedAt(now)
            .expiresAt(now.plus(10, ChronoUnit.MINUTES)) // Nouvelle durée de vie
            .claim("email", email)
            .claim("scope", scope) // Inclure les mêmes scopes
            .build();
    String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

    // Retourner les nouveaux tokens
    Map<String, String> tokens = new HashMap<>();
    tokens.put("Access_Token", newAccessToken);
    tokens.put("Refresh_Token", refreshToken); // Réutiliser le Refresh Token existant

    return tokens;
}


}