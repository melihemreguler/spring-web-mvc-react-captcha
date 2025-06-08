package tr.edu.duzce.mf.bm.bm470captcha.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.exception.CaptchaException;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;

import java.util.List;

@Service
@Transactional
public class CaptchaService {

    private final ICaptchaRepository captchaRepository;

    public CaptchaService(ICaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    /**
     * Veritabanından rastgele bir Captcha döner.
     */
    public Captcha getRandomCaptcha() {
        return captchaRepository.findRandomCaptcha();
    }

    /**
     * Kullanıcının girdiği cevap ile doğru Captcha değerini karşılaştırır.
     *
     * @param captchaId doğrulama yapılacak Captcha'nın id'si
     * @param userInput kullanıcının yazdığı değer
     * @return true -> doğru girdi, false -> yanlış girdi
     */
    public boolean validateCaptcha(Long captchaId, String userInput) {
        try {
            Captcha captcha = captchaRepository.findById(captchaId);
            if (captcha == null) {
                throw new CaptchaException("Captcha bulunamadı, geçersiz ID.");
            }
            return captcha.getTextValue().equalsIgnoreCase(userInput.trim());
        } catch (Exception e) {
            throw new CaptchaException("Captcha doğrulama sırasında bir hata oluştu.", e);
        }
    }

    /**
     * Sayfalama ile Captcha listesini döner.
     *
     * @param page sayfa numarası (0 tabanlı)
     * @param size sayfa boyutu
     * @return captcha listesi
     */
    public List<Captcha> getAllCaptchas(int page, int size) {
        return captchaRepository.findAll(page, size);
    }
    public long getTotalCount() {
        return captchaRepository.count();
    }

    /**
     * Yeni Captcha kaydeder.
     *
     * @param captcha kaydedilecek captcha nesnesi
     */
    public void addCaptcha(Captcha captcha) {
        captchaRepository.save(captcha);
    }

    /**
     * Belirtilen ID'ye sahip Captcha'yı siler.
     *
     * @param id silinecek captcha id
     */
    public void deleteCaptcha(Long id) {
        captchaRepository.deleteById(id);
    }
}
