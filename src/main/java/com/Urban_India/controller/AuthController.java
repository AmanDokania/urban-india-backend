package com.Urban_India.controller;

import com.Urban_India.entity.Image;
import com.Urban_India.payload.IdTokenRequestDto;
import com.Urban_India.payload.JwtAuthResponse;
import com.Urban_India.payload.LoginDto;
import com.Urban_India.payload.RegisterDto;
import com.Urban_India.repository.ImageRepositroy;
import com.Urban_India.service.AuthService;
import com.Urban_India.service.ImageDataService;
import com.Urban_India.util.ImageUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private ObjectMapper mapper;
    private AuthService authService;
    private ImageDataService imageDataService;

    @PostMapping(value = {"auth/signup" , "auth/register"})
    public ResponseEntity<String> signUp(@Valid @RequestParam("data") String data, @RequestParam("file") MultipartFile file) throws IOException
    {

        Image image =imageDataService.saveImage(file);
        // converting string to json
        RegisterDto registerDto = mapper.readValue(data, RegisterDto.class);
        registerDto.setImages(image);

        String response = authService.register(registerDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"auth/login","auth/signin"})
    private ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        String token=authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping(value = {"/auth/google"})
    private ResponseEntity<JwtAuthResponse> googleAuthentication(@RequestBody IdTokenRequestDto idTokenRequest) throws GeneralSecurityException, IOException {

        Pair<Boolean, String> pair = authService.loginOAuth(idTokenRequest);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        if (pair.getLeft()){
            jwtAuthResponse.setAccessToken(pair.getRight());
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        }
        jwtAuthResponse.setEmail(pair.getRight());
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.NOT_FOUND);
    }

    //    @PostMapping(value = {"/auth/google"})
//    private ResponseEntity<String> googleAuthentication(@RequestBody String idTokenString) throws GeneralSecurityException, IOException {
//
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport,jsonFactory)
//                // Specify the CLIENT_ID of the app that accesses the backend:
//                .setAudience(Collections.singletonList("'101242721034-svf9du5sbf1rehbmjqukg907v03p25dt.apps.googleusercontent.com'"))
//                // Or, if multiple clients access the backend:
//                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//                .build();
//
//// (Receive idTokenString by HTTPS POST)
//
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (idToken != null) {
//            GoogleIdToken.Payload payload = idToken.getPayload();
//
//            // Print user identifier
//            String userId = payload.getSubject();
//            System.out.println("User ID: " + userId);
//
//            // Get profile information from payload
//            String email = payload.getEmail();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");
//
//            // Use or store profile information
//            // ...
//
//        } else {
//            System.out.println("Invalid ID token.");
//        }
//        return new ResponseEntity<>("user registered successfully",HttpStatus.OK);
//    }
}
