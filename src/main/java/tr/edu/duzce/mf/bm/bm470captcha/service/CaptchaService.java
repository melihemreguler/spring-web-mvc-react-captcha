package tr.edu.duzce.mf.bm.bm470captcha.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.exception.CaptchaException;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class CaptchaService {

    @PersistenceContext
    private EntityManager entityManager;

    private final Random random = new Random();

    /**
     * Veritabanından rastgele bir Captcha döner.
     */
    public Captcha getRandomCaptcha() {
        try {
            // CriteriaBuilder oluştur
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            // Captcha tipinde bir CriteriaQuery oluştur
            CriteriaQuery<Captcha> cq = cb.createQuery(Captcha.class);
            // FROM Captcha c
            Root<Captcha> root = cq.from(Captcha.class);
            // SELECT c
            cq.select(root);

            // Sorguyu çalıştır
            List<Captcha> captchas = entityManager.createQuery(cq).getResultList();

            if (captchas.isEmpty()) {
                throw new CaptchaException("Captcha veritabanında kayıt bulunamadı.");
            }

            int randomIndex = random.nextInt(captchas.size());
            return captchas.get(randomIndex);
        } catch (Exception e) {
            throw new CaptchaException("Captcha veritabanı sorgusu sırasında bir hata oluştu.", e);
        }
    }

    /**
     * Kullanıcının girdiği cevap ile doğru Captcha değerini karşılaştırır.
     *
     * @param captchaId   doğrulama yapılacak Captcha'nın id'si
     * @param userInput   kullanıcının yazdığı değer
     * @return true -> doğru girdi, false -> yanlış girdi
     */
    public boolean validateCaptcha(Long captchaId, String userInput) {
        try {
            Captcha captcha = entityManager.find(Captcha.class, captchaId);
            if (captcha == null) {
                throw new CaptchaException("Captcha bulunamadı, geçersiz ID.");
            }
            return captcha.getTextValue().equalsIgnoreCase(userInput.trim());
        } catch (Exception e) {
            throw new CaptchaException("Captcha doğrulama sırasında bir hata oluştu.", e);
        }
    }
}
