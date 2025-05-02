package com.authservice.auth_service.controllers;

import com.authservice.auth_service.dtos.LoginRequestDTO;
import com.authservice.auth_service.dtos.LoginResponseDTO;
import com.authservice.auth_service.dtos.RequestStatus;
import com.authservice.auth_service.exceptions.WrongPasswordException;
import com.authservice.auth_service.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) throws WrongPasswordException {
        try{
            String token = authService.login(request.getEmail(), request.getPassword());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setStatus(RequestStatus.SUCCESS);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("AUTH_TOKEN", token);

            ResponseEntity<LoginResponseDTO> response = new ResponseEntity<>(loginResponseDTO, headers, HttpStatus.OK);

            return response;
        } catch (Exception e) {
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setStatus(RequestStatus.FAILED);

            ResponseEntity<LoginResponseDTO> response =
                    new ResponseEntity<>(loginResponseDTO, null, HttpStatus.BAD_REQUEST);

            return response;
        }
    }
}
