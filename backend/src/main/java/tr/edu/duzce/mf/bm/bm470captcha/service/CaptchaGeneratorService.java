package tr.edu.duzce.mf.bm.bm470captcha.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaGeneratorRepository;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;
import tr.edu.duzce.mf.bm.bm470captcha.util.CaptchaGenerator;

@Service
@Transactional
public class CaptchaGeneratorService {

    private final ICaptchaGeneratorRepository captchaRepository;

    @Autowired
    public CaptchaGeneratorService(ICaptchaGeneratorRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public void generateAndSaveCaptchas(int count) {
        for (int i = 0; i < count; i++) {
            String text = CaptchaGenerator.generateRandomText(6);  // 6 karakterlik rastgele metin
            byte[] imageBytes = CaptchaGenerator.generateCaptchaImageBytes(text);  // Resmi byte[] olarak üret

            Captcha captcha = new Captcha();
            captcha.setTextValue(text);
            captcha.setImage(imageBytes);  // BLOB olarak kaydet

            captchaRepository.save(captcha);
        }
    }
}
