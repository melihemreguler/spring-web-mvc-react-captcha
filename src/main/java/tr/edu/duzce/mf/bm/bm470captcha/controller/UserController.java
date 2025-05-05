package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.bm470captcha.service.UserVerificationService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserVerificationService userVerificationService;
    private final MessageSource messageSource;

    @Autowired
    public UserController(UserVerificationService userVerificationService, MessageSource messageSource) {
        this.userVerificationService = userVerificationService;
        this.messageSource = messageSource;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam("username") String username,
                                                         @RequestParam("password") String password,
                                                         Locale locale) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean isAuthenticated = userVerificationService.verifyLogin(username, password);
            response.put("success", isAuthenticated);

            String messageKey = isAuthenticated ? "login.success" : "login.error";
            response.put("message", messageSource.getMessage(messageKey, null, locale));

            return isAuthenticated
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", messageSource.getMessage("login.exception", new Object[]{e.getMessage()}, locale));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestParam("username") String username,
                                                            @RequestParam("password") String password,
                                                            Locale locale) {

        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("request path: /api/user/register");
            userVerificationService.registerUser(username, password);
            response.put("success", true);
            response.put("message", messageSource.getMessage("register.success", null, locale));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", messageSource.getMessage("register.error", new Object[]{e.getMessage()}, locale));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

