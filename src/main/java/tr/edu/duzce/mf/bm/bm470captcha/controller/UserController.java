package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.bm470captcha.service.UserVerificationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserVerificationService userVerificationService;

    @Autowired
    public UserController(UserVerificationService userVerificationService) {
        this.userVerificationService = userVerificationService;
    }

    /**
     * Kullanıcı girişini doğrulayan endpoint.
     */
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam("username") String username,
                                                         @RequestParam("password") String password) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean isAuthenticated = userVerificationService.verifyLogin(username, password);
            response.put("success", isAuthenticated);

            if (isAuthenticated) {
                response.put("message", "Giriş başarılı!");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Kullanıcı adı veya şifre hatalı!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Giriş sırasında hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Yeni kullanıcı kaydeden endpoint.
     */
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestParam("username") String username,
                                                            @RequestParam("password") String password) {

        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("request path: /api/user/register");
            logger.info("Kullanıcı adı: " + username);
            logger.info("Şifre: " + password);
            logger.info("kayit isteği yapıldı.");
            userVerificationService.registerUser(username, password);
            response.put("success", true);
            response.put("message", "Kayit basarili!");
            System.out.println(response.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            e.printStackTrace();
            System.out.println(response.toString()+"hata?");
            response.put("message", "Kayıt sırasında hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    }
}
