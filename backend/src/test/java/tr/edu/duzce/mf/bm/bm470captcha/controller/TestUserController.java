package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.web.WebAppConfiguration;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebAppInitializer;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebConfig;
import tr.edu.duzce.mf.bm.bm470captcha.config.AppConfig;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        WebAppInitializer.class,
        WebConfig.class,
        AppConfig.class
})
@Transactional
@WebAppConfiguration
public class TestUserController {

    @Autowired
    private UserController userController;

    @Autowired
    private MessageSource messageSource;

    private Locale trLocale;

    @BeforeEach
    public void init() {
        trLocale = Locale.forLanguageTag("tr-TR");
    }

    /**
     * Her test için benzersiz user adı üretir,
     * böylece DB’de çakışma olmaz.
     */
    private String uniqueUsername() {
        return "user_" + UUID.randomUUID();
    }

    @Test
    public void testRegisterUser_Success() {
        String username = uniqueUsername();

        // WHEN
        ResponseEntity<Map<String, Object>> response =
                userController.registerUser(username, "P@ssw0rd", trLocale);

        // THEN: 201 Created
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        Assertions.assertTrue((Boolean) body.get("success"));

        // messages.properties içindeki karşılık
        String expected = messageSource.getMessage("register.success", null, trLocale);
        Assertions.assertEquals(expected, body.get("message"));
    }

    @Test
    public void testLoginUser_Success() {
        String username = uniqueUsername();

        // önce kayıt ol
        userController.registerUser(username, "Secret123", trLocale);

        // WHEN
        ResponseEntity<Map<String, Object>> response =
                userController.loginUser(username, "Secret123", trLocale);

        // THEN: 200 OK
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        Assertions.assertTrue((Boolean) body.get("success"));

        String expected = messageSource.getMessage("login.success", null, trLocale);
        Assertions.assertEquals(expected, body.get("message"));
    }

    @Test
    public void testLoginUser_UserNotFound() {
        String username = uniqueUsername();

        // WHEN: hiç kayıt olmadı bu username ile
        ResponseEntity<Map<String, Object>> response =
                userController.loginUser(username, "anyPass", trLocale);

        // THEN: 500 Internal Server Error (servis wrap’lediği genel mesaj)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        Assertions.assertFalse((Boolean) body.get("success"));

        // e.getMessage() artık hep "Kullanıcı doğrulama işlemi sırasında bir hata oluştu."
        String detail = "Kullanıcı doğrulama işlemi sırasında bir hata oluştu.";
        String expected = messageSource.getMessage("login.exception", new Object[]{ detail }, trLocale);
        Assertions.assertEquals(expected, body.get("message"));
    }

    @Test
    public void testLoginUser_WrongPassword() {
        String username = uniqueUsername();

        // önce doğru şifreyle kayıt
        userController.registerUser(username, "RightPass", trLocale);

        // WHEN: yanlış şifre gir
        ResponseEntity<Map<String, Object>> response =
                userController.loginUser(username, "WrongPass", trLocale);

        // THEN: yine 500 Internal Server Error, genel hata mesajı
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        Assertions.assertFalse((Boolean) body.get("success"));

        String detail = "Kullanıcı doğrulama işlemi sırasında bir hata oluştu.";
        String expected = messageSource.getMessage("login.exception", new Object[]{ detail }, trLocale);
        Assertions.assertEquals(expected, body.get("message"));
    }

    @Test
    public void testRegisterUser_Duplicate() {
        String username = uniqueUsername();

        // GIVEN: bir kere kayıt ol
        ResponseEntity<Map<String, Object>> first =
                userController.registerUser(username, "Dup123!", trLocale);
        Assertions.assertEquals(HttpStatus.CREATED.value(), first.getStatusCodeValue());

        // WHEN: aynı username ile tekrar dene
        ResponseEntity<Map<String, Object>> response =
                userController.registerUser(username, "Dup123!", trLocale);

        // THEN: 400 Bad Request
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        Assertions.assertFalse((Boolean) body.get("success"));

        String detail = "Kullanıcı kaydetme işlemi sırasında bir hata oluştu.";
        String expected = messageSource.getMessage("register.error", new Object[]{ detail }, trLocale);
        Assertions.assertEquals(expected, body.get("message"));
    }
}
