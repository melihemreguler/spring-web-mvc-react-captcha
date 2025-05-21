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
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaGeneratorService;

import java.util.List;
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
    @Autowired
    private CaptchaGeneratorService captchaGeneratorService;

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
    public void testValidateCaptchaWithGenerated() {
        // 1 CAPTCHA üret
        captchaGeneratorService.generateAndSaveCaptchas(1);

        // En son CAPTCHA'yı çek (page=0, size=1 → tek en yeni kayıt)
        var captchaListResponse = captchaController.getAllCaptchas(0, 1);
        List<Map<String, Object>> captchas = (List<Map<String, Object>>)
                ((Map<?, ?>) captchaListResponse.getBody()).get("captchas");

        Assertions.assertFalse(captchas.isEmpty(), "Captcha listesi boş dönmemeli!");

        Map<String, Object> lastCaptcha = captchas.get(0);
        Long captchaId = ((Number) lastCaptcha.get("id")).longValue();
        String correctText = (String) lastCaptcha.get("textValue");

        // CAPTCHA doğrulaması yap
        var response = captchaController.validateCaptcha(captchaId, correctText, Locale.forLanguageTag("tr-TR"));

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

    @Test
    public void testGetAllCaptchasSuccess() {
        int page = 0;
        int size = 10;

        var response = captchaController.getAllCaptchas(page, size);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        Assertions.assertTrue(body.containsKey("totalCount"));
        Assertions.assertTrue(body.containsKey("captchas"));
    }

    @Test
    @Rollback(false)
    public void testAddCaptchaSuccess() {
        Captcha captcha = new Captcha();
        captcha.setTextValue("TEST12");
        captcha.setImage(new byte[]{1, 2, 3}); // örnek görsel verisi

        var response = captchaController.addCaptcha(captcha, Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(201, response.getStatusCodeValue());
        Assertions.assertTrue((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha eklendi!", response.getBody().get("message"));
    }

    @Test
    public void testAddCaptchaFailure() {
        // Null değer verilerek başarısızlık senaryosu tetikleniyor
        Captcha captcha = null;

        var response = captchaController.addCaptcha(captcha, Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(500, response.getStatusCodeValue());
        Assertions.assertFalse((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha eklenirken hata oluştu!", response.getBody().get("message"));
    }

    @Test
    @Rollback(false)
    public void testDeleteCaptchaSuccess() {
        // Önce bir captcha ekleyelim
        Captcha captcha = new Captcha();
        captcha.setTextValue("DEL123");
        captcha.setImage(new byte[]{4, 5, 6});
        captchaController.addCaptcha(captcha, Locale.forLanguageTag("tr-TR"));

        // Son eklenenin ID’sini getirme yöntemi (örneğin listelemeden alabiliriz)
        var captchas = captchaController.getAllCaptchas(0, 1);
        var list = (List<Map<String, Object>>) ((Map<?, ?>) captchas.getBody()).get("captchas");
        Long id = (Long) list.get(0).get("id");

        var response = captchaController.deleteCaptcha(id, Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertTrue((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha silindi!", response.getBody().get("message"));
    }

    @Test
    public void testDeleteCaptchaFailure() {
        // Geçersiz ID kullanılarak silme
        Long invalidId = -1L;

        var response = captchaController.deleteCaptcha(invalidId, Locale.forLanguageTag("tr-TR"));

        Assertions.assertEquals(500, response.getStatusCodeValue());
        Assertions.assertFalse((Boolean) response.getBody().get("success"));
        Assertions.assertEquals("Captcha silinirken hata oluştu!", response.getBody().get("message"));
    }

}
