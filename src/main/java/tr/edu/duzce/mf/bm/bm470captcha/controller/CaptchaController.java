package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * Rastgele bir captcha getirir.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<Map<String, Object>> getCaptcha() {
        Captcha captcha = captchaService.getRandomCaptcha();

        Map<String, Object> response = new HashMap<>();
        if (captcha == null) {
            response.put("success", false);
            response.put("message", "Hiç captcha bulunamadı!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Captcha resmini base64 encode et
        String base64Image = Base64.getEncoder().encodeToString(captcha.getImage());

        response.put("success", true);
        response.put("captchaId", captcha.getId());
        response.put("captchaImage", base64Image);

        return ResponseEntity.ok(response);
    }

    /**
     * Kullanıcının captcha girişini doğrular.
     */
    @PostMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<Map<String, Object>> validateCaptcha(@RequestParam("captchaId") Long captchaId,
                                                               @RequestParam("captchaInput") String captchaInput) {
        boolean isValid = captchaService.validateCaptcha(captchaId, captchaInput);

        Map<String, Object> response = new HashMap<>();
        response.put("success", isValid);

        if (isValid) {
            response.put("message", "Captcha doğru!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Captcha yanlış!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
