package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import tr.edu.duzce.mf.bm.bm470captcha.config.WebAppInitializer;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebConfig;
import tr.edu.duzce.mf.bm.bm470captcha.config.AppConfig;

import java.util.Locale;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        WebAppInitializer.class,
        WebConfig.class,
        AppConfig.class
})
@Transactional
@WebAppConfiguration
public class TestCaptchaController {

    @Autowired
    private CaptchaController captchaController;

    @Test
    public void testGetCaptcha() {
        // WHEN: Captcha alınmak istendiğinde
        var result = captchaController.getCaptcha(Locale.forLanguageTag("tr-TR"));

        // THEN: Başarılı dönüş ve base64 görsel içermeli
        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertTrue((Boolean) result.getBody().get("success"));
        Assertions.assertNotNull(result.getBody().get("captchaId"));
        Assertions.assertNotNull(result.getBody().get("captchaImage"));
    }

    @Test
    @Rollback(false)
    public void testValidateCaptchaDogru() {
        var captchaResponse = captchaController.getCaptcha(Locale.forLanguageTag("tr-TR"));
        Long captchaId = (Long) captchaResponse.getBody().get("captchaId");

        String correctCaptchaInput = "IR0FIP"; // <- Bu test veritabanındaki gerçek değer olmalı!

        var response = captchaController.validateCaptcha(103L, correctCaptchaInput, Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha doğru!", response.getBody().get("message"));
    }

    @Test
    public void testValidateCaptchaYanlis() {
        var captchaResponse = captchaController.getCaptcha(Locale.forLanguageTag("tr-TR"));
        Long captchaId = (Long) captchaResponse.getBody().get("captchaId");

        var response = captchaController.validateCaptcha(captchaId, "yanlisGirdi", Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertFalse((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha yanlış!", response.getBody().get("message"));
    }
}
