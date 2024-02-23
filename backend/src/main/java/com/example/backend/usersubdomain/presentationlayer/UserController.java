package com.example.backend.usersubdomain.presentationlayer;

import com.example.backend.usersubdomain.buisnesslayer.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "https://movingexpress.systems"},allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/movingexpress/users")
public class UserController {
    private final UserService userService;

//    @DeleteMapping
//    public ResponseEntity<Void> deleteCustomer(@AuthenticationPrincipal OidcUser principal) {
//        String userId = principal.getSubject();
//        log.info("Delete customer with userId: {}", userId);
//
//        userService.deleteCustomer(userId);
//
//        return ResponseEntity.noContent().build();
//
//    }


    @GetMapping
    public ResponseEntity<UserResponseModel> getUserByUserId(@AuthenticationPrincipal OidcUser principal, @RequestParam Map<String, String> requestParams) {
        if (requestParams.containsKey("simpleCheck") && requestParams.get("simpleCheck").equals("true")) {
            if(userService.checkIfUserExistsByUserId(principal.getSubject()))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserResponseModel customerResponse = userService.getUserByUserId(principal.getSubject());

        return ResponseEntity.ok(customerResponse);

    }


    @PostMapping
    public ResponseEntity<UserResponseModel> addUser(@AuthenticationPrincipal OidcUser principal,
                                                     @Valid @RequestBody UserRequestModel customerRequest) {
        String userId = principal.getSubject();
        UserResponseModel customerResponse = userService.addUser(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);

    }

    @PutMapping
    public ResponseEntity<UserResponseModel> updateUser(@AuthenticationPrincipal OidcUser principal,
                                                        @Valid @RequestBody UserRequestModel customerRequest) {
        String userId = principal.getSubject();
        return ResponseEntity.ok(userService.updateUser(customerRequest, userId));

    }
}
