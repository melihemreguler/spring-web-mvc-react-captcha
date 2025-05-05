package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import tr.edu.duzce.mf.bm.bm470captcha.config.WebAppInitializer;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebConfig;
import tr.edu.duzce.mf.bm.bm470captcha.config.AppConfig;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        WebAppInitializer.class,
        WebConfig.class,
        AppConfig.class
})
@Transactional
public class TestCaptchaController {

    @Autowired
    private CaptchaController captchaController;

    @Test
    public void testGetCaptcha() {
        // GIVEN: CaptchaService içinde en az bir Captcha var.

        // WHEN: /getcaptcha endpoint'ine istek atıldığında
        ResponseEntity<Map<String, Object>> response = captchaController.getCaptcha();

        // THEN: Başarılı bir şekilde captcha dönmeli
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue((Boolean) response.getBody().get("success"));
        Assertions.assertNotNull(response.getBody().get("captchaId"));
        Assertions.assertNotNull(response.getBody().get("captchaImage"));
    }

    @Test
    @Rollback(value = false)
    public void testValidateCaptchaDogru() {
        // GIVEN: Doğru bir captcha alınır
        ResponseEntity<Map<String, Object>> captchaResponse = captchaController.getCaptcha();
        Long captchaId = (Long) captchaResponse.getBody().get("captchaId");

        // Kullanıcının doğru girdiği varsayımıyla CaptchaService sabit test datası sağlar
        // WHEN: Doğru captchaInput ile validate endpoint'ine istek atılır
        ResponseEntity<Map<String, Object>> response =
                captchaController.validateCaptcha(captchaId, "doğru-değeri-burada-yerine-koy");

        // THEN: Doğrulama başarılı olur
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha doğru!", response.getBody().get("message"));
    }

    @Test
    public void testValidateCaptchaYanlis() {
        // GIVEN: Geçerli bir captchaId
        ResponseEntity<Map<String, Object>> captchaResponse = captchaController.getCaptcha();
        Long captchaId = (Long) captchaResponse.getBody().get("captchaId");

        // WHEN: Yanlış bir captchaInput ile validate endpoint'ine istek atılır
        ResponseEntity<Map<String, Object>> response =
                captchaController.validateCaptcha(captchaId, "yanlisGirdi");

        // THEN: Doğrulama başarısız olur
        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertFalse((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha yanlış!", response.getBody().get("message"));
    }
}
