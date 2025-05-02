package com.authservice.auth_service.services;

import com.authservice.auth_service.exceptions.UserNotFoundException;
import com.authservice.auth_service.exceptions.WrongPasswordException;
import com.authservice.auth_service.models.Session;
import com.authservice.auth_service.models.SessionStatus;
import com.authservice.auth_service.models.User;
import com.authservice.auth_service.repositories.AuthRepository;
import com.authservice.auth_service.repositories.SessionRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService {
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRepository sessionRepository;
    private final AuthRepository authRepository;

    private SecretKey key = Keys.hmacShaKeyFor(
            "namanisveryveryveryveryveryveryverycool"
                    .getBytes(StandardCharsets.UTF_8));

    public AuthService(RestTemplate restTemplate, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepository sessionRepository, AuthRepository authRepository) {
        this.restTemplate = restTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
        this.authRepository = authRepository;
    }

    public String login(String email, String password) throws WrongPasswordException {
        User user = restTemplate.getForObject("http://localhost:8080/users/"+email, User.class);


        if(user == null) {
            throw new UserNotFoundException("User with email: "+ email + " not found");
        }
        boolean matches = bCryptPasswordEncoder.matches(password, user.getPaswordHash());

        if(matches) {

            String token = createJWTToken(user.getId(), new ArrayList<>(), user.getEmail());

            Session session = new Session();
            session.setSessionStatus(SessionStatus.ACTIVE);
            session.setToken(token);

            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date datePlus30Days = calendar.getTime();
            session.setExpiringAt(datePlus30Days);

            sessionRepository.save(session);

            return token;
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    private String createJWTToken(Long useId, List<String> roles, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", useId);
        claims.put("roles", roles);
        claims.put("email", email);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date datePlus30Days = calendar.getTime();

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(datePlus30Days)
                .signWith(key)
                .compact();
    }
}
