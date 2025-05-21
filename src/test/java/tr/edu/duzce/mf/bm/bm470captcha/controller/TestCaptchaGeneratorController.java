package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.web.WebAppConfiguration;
import tr.edu.duzce.mf.bm.bm470captcha.config.AppConfig;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebAppInitializer;
import tr.edu.duzce.mf.bm.bm470captcha.config.WebConfig;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        WebAppInitializer.class,
        WebConfig.class,
        AppConfig.class
})
@WebAppConfiguration
@Transactional
public class TestCaptchaGeneratorController {

    @Autowired
    private CaptchaGeneratorController captchaGeneratorController;

    @Autowired
    private ICaptchaRepository captchaRepository;

    @BeforeEach
    public void cleanDatabase() {
        // Eğer isterseniz buradan tüm Captcha tablosunu temizleyebilirsiniz.
        // captchaRepository.findAll(0, Integer.MAX_VALUE)
        //         .forEach(c -> captchaRepository.deleteById(c.getId()));
    }

    @Test
    public void testGenerateSingleCaptcha() {
        // GIVEN: Başlangıçtaki toplam captcha sayısı
        long before = captchaRepository.count();

        // WHEN: Tek bir CAPTCHA üret endpoint'i çağrıldığında
        ResponseEntity<String> response = captchaGeneratorController.generateCaptcha();

        // THEN: 200 OK ve doğru mesaj dönmeli
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(
                "1 base64 CAPTCHA başarıyla üretildi ve kaydedildi.",
                response.getBody()
        );

        // AND: DB'de bir adet yeni kayıt olmalı
        long after = captchaRepository.count();
        Assertions.assertEquals(before + 1, after);
    }

    @Test
    public void testGenerateHundredCaptchas() {
        // GIVEN
        long before = captchaRepository.count();

        // WHEN: 100 CAPTCHA üret endpoint'i çağrıldığında
        ResponseEntity<String> response = captchaGeneratorController.generateCaptchas();

        // THEN: 200 OK ve doğru mesaj dönmeli
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(
                "100 base64 CAPTCHA başarıyla üretildi ve kaydedildi.",
                response.getBody()
        );

        // AND: DB'de 100 yeni kayıt olmalı
        long after = captchaRepository.count();
        Assertions.assertEquals(before + 100, after);
    }
}
