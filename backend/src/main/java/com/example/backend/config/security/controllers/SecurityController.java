package com.example.backend.config.security.controllers;

import com.example.backend.config.security.data.UserInfoResponseModel;
import com.example.backend.config.security.service.Auth0LoginService;
import com.example.backend.config.security.service.Auth0ManagementService;
import com.example.backend.usersubdomain.buisnesslayer.UserService;
import com.example.backend.usersubdomain.presentationlayer.UserRequestModel;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
@CrossOrigin(origins = {"http://localhost:3000", "https://movingexpress.systems"} ,allowCredentials = "true")
@RestController
@Generated
@RequiredArgsConstructor
@RequestMapping("/api/v1/movingexpress/security")
public class SecurityController {
    @Value("${frontend.url}")
    private String frontendDomain;

    @Value("${backend.url}")
    private String backendDomain;

    private final Auth0LoginService auth0LoginService;
    private final Auth0ManagementService auth0ManagementService;
    private final UserService userService;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectAfterLogin(@AuthenticationPrincipal OidcUser principal) throws IOException, InterruptedException {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(backendDomain + "oauth2/authorization/okta")).build();
        }
        if(!userService.checkIfUserExistsByUserId(principal.getSubject())){
            UserRequestModel userRequestModel = UserRequestModel.builder()
                    .userId(principal.getSubject())
                    .email(principal.getEmail())
                    .firstName(principal.getNickName())
                    .profilePictureUrl(principal.getClaim("picture"))
                    .build();
            userService.addUser(userRequestModel);
        }
        //handle apple, google and facebook login
        if (principal.getSubject().contains("apple") || principal.getSubject().contains("google-oauth2")
                || principal.getSubject().contains("facebook")) {
            return ResponseEntity.status(302)
                    .location(URI.create(frontendDomain + "external")).build();
        }
        if (principal.getSubject().contains("auth0")) {
            if (!principal.getClaims().containsKey("https://movingexpress.com/roles")
                    || principal.getClaim("https://movingexpress.com/roles").toString().equals("[]")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(frontendDomain + "logout")).build();
            }
        }
        return auth0LoginService.getVoidResponseEntity(principal);
    }


    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponseModel> getUserInfo(@AuthenticationPrincipal OidcUser principal) throws IOException, InterruptedException {
        if(principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String userId = principal.getSubject();
        return ResponseEntity.ok().body(auth0ManagementService.getUserInfo(userId));
    }
}
