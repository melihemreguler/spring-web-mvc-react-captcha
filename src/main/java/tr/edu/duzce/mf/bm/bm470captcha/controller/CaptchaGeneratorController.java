package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaGeneratorService;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaService;
import tr.edu.duzce.mf.bm.bm470captcha.util.CaptchaGenerator;

@RestController
@RequestMapping("/api/captchas")
public class CaptchaGeneratorController {

    private final CaptchaGeneratorService captchaService;

    @Autowired
    public CaptchaGeneratorController(CaptchaGeneratorService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateCaptchas() {
        captchaService.generateAndSaveCaptchas(100);
        return ResponseEntity.ok("100 base64 CAPTCHA başarıyla üretildi ve kaydedildi.");
    }
}
