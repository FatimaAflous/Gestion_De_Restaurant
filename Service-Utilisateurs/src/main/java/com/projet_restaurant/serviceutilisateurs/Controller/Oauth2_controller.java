package com.projet_restaurant.serviceutilisateurs.Controller;
import com.projet_restaurant.serviceutilisateurs.Dto.LoginRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println(email + " and " + password);

        // Vérifier l'authentification
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Générer les tokens
        Instant now = Instant.now();
// Récupérer le nom d'utilisateur de l'utilisateur
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String username = userDetails.getUsername(); // récupère le nom d'utilisateu

        // Récupérer les autorités de l'utilisateur
        String scope = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(userDetails.getUsername()) // Nom d'utilisateur correct
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .claim("username", userDetails.getUsername()) // Ajout du nom d'utilisateur correct
                .claim("scope", scope)
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName()) // email
                .subject(username) // nom d'utilisateur
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .build();
        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();
        // Ajouter le refreshToken dans un cookie sécurisé
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // Non accessible via JavaScript
                .secure(true) // Seulement via HTTPS
                .path("/") // Disponible sur toutes les routes
                .maxAge(15 * 60) // 15 minutes en secondes
                .sameSite("Strict") // Empêche l'envoi cross-origin
                .build();

        // Créer également un cookie pour le accessToken si nécessaire (pas obligatoire)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true) // Non accessible via JavaScript
                .secure(true) // Transmission seulement via HTTPS
                .path("/") // Disponible sur toutes les routes
                .maxAge(5 * 60) // 10 minutes en secondes
                .sameSite("Strict") // Empêche l'envoi cross-origin
                .build();

        Map<String, String> response = new HashMap<>();
        response.put("Access_Token", accessToken); // Le token d'accès reste dans la réponse
        response.put("Refresh_Token", refreshToken); // Le token d'accès reste dans la réponse

        System.out.println("User authenticated: " + authenticate.getName() + " with role(s): " + scope);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString()) // Ajout du cookie dans l'entête de la réponse
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString()) // Ajout du accessToken comme cookie
                .body(response);
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!userDetails.getUsername().equals(email)) {
            throw new IllegalArgumentException("Les informations utilisateur ne correspondent pas.");
        }

        String updatedScope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

    // Générer un nouveau Access Token
        Instant now = Instant.now();
        JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .claim("email", email)
                .claim("scope", updatedScope)
                .build();
        String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();
        // Créer également un cookie pour le accessToken si nécessaire (pas obligatoire)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true) // Non accessible via JavaScript
                .secure(true) // Transmission seulement via HTTPS
                .path("/") // Disponible sur toutes les routes
                .maxAge(6 * 60) // 10 minutes en secondes
                .sameSite("Strict") // Empêche l'envoi cross-origin
                .build();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("Access_Token", newAccessToken);
        tokens.put("Refresh_Token", refreshToken);

        return tokens;
}


}