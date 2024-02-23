package com.example.backend.config.security.service;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.net.URI;
@Service
@Generated
@RequiredArgsConstructor
public class Auth0LoginService {
    @Value("${frontend.url}")
    private String frontendDomain;
    @Value("${website.domain}")
    private String domain;

    public ResponseEntity<Void> getVoidResponseEntity(@AuthenticationPrincipal OidcUser principal) {
        HttpHeaders headers = new HttpHeaders();
        String formattedDomain = frontendDomain.replace("https://", "").replace("http://", "")
                .split(":")[0].replace("/","");
        ResponseCookie cookie = ResponseCookie.from("id_token",principal.getIdToken().getTokenValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .domain(formattedDomain)
                .sameSite("None")
                .build();
        ResponseCookie authCookie = ResponseCookie.from("isAuthenticated", "true")
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();
        ResponseCookie accessPermissionCookie = ResponseCookie.from("accessPermission", principal.getAuthorities().toString()
                        .replace(",","-").replace(" ",""))
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();
        String picture = principal.getClaim("picture");
        ResponseCookie pictureCookie = ResponseCookie.from("picture", picture)
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, authCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessPermissionCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, pictureCookie.toString());

        if (principal.getClaim("https://movingexpress.com/roles").toString().contains("Shipment_Reviewer")) {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain + "Reviewer")).build();
        } else if (principal.getClaim("https://movingexpress.com/roles").toString().contains("Shipment_Estimator")) {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain + "Estimator")).build();
        } else if (principal.getClaim("https://movingexpress.com/roles").toString().contains("Truck_Driver")) {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain + "Driver")).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain + "Home")).build();
        }
    }
}
