package tr.edu.duzce.mf.bm.bm470captcha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaService;

import java.util.*;
import java.util.Base64;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/getcaptcha", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getCaptcha(Locale locale) {
        Captcha captcha = captchaService.getRandomCaptcha();
        Map<String, Object> response = new HashMap<>();

        if (captcha == null) {
            response.put("success", false);
            response.put("message", messageSource.getMessage("captcha.notfound", null, locale));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String base64Image = Base64.getEncoder().encodeToString(captcha.getImage());
        response.put("success", true);
        response.put("captchaId", captcha.getId());
        response.put("captchaImage", base64Image);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<Map<String, Object>> validateCaptcha(@RequestParam("captchaId") Long captchaId,
                                                               @RequestParam("captchaInput") String captchaInput,
                                                               Locale locale) {
        boolean isValid = captchaService.validateCaptcha(captchaId, captchaInput);
        Map<String, Object> response = new HashMap<>();
        response.put("success", isValid);

        String messageKey = isValid ? "captcha.correct" : "captcha.incorrect";
        response.put("message", messageSource.getMessage(messageKey, null, locale));

        return isValid
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCaptchas(@RequestParam(name = "page") int page,
                                            @RequestParam(name = "size") int size) {
        try {
            List<Captcha> captchas = captchaService.getAllCaptchas(page, size);
            long totalCount = captchaService.getTotalCount();

            List<Map<String, Object>> result = new ArrayList<>();
            for (Captcha captcha : captchas) {
                Map<String, Object> captchaMap = new HashMap<>();
                captchaMap.put("id", captcha.getId());
                captchaMap.put("textValue", captcha.getTextValue());

                if (captcha.getImage() != null) {
                    captchaMap.put("image", Base64.getEncoder().encodeToString(captcha.getImage()));
                } else {
                    captchaMap.put("image", "");
                }

                result.add(captchaMap);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", totalCount);
            response.put("captchas", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sunucu hatası: " + e.getMessage());
        }
    }





    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCaptcha(@RequestBody Captcha captcha, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        try {
            captchaService.addCaptcha(captcha);
            response.put("success", true);
            response.put("message", messageSource.getMessage("captcha.added", null, locale));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", messageSource.getMessage("captcha.add.error", null, locale));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCaptcha(@PathVariable Long id, Locale locale) {
        Map<String, Object> response = new HashMap<>();
        try {
            captchaService.deleteCaptcha(id);
            response.put("success", true);
            response.put("message", messageSource.getMessage("captcha.deleted", null, locale));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", messageSource.getMessage("captcha.delete.error", null, locale));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
