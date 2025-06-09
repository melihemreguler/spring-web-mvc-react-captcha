package tr.edu.duzce.mf.bm.bm470captcha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import tr.edu.duzce.mf.bm.bm470captcha.service.CaptchaGeneratorService;
import tr.edu.duzce.mf.bm.bm470captcha.service.UserVerificationService;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;
import tr.edu.duzce.mf.bm.bm470captcha.repository.IUserRepository;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CaptchaGeneratorService captchaGeneratorService;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private ICaptchaRepository captchaRepository;

    @Autowired
    private IUserRepository userRepository;

    private boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!initialized) {
            initializeData();
            initialized = true;
        }
    }

    private void initializeData() {
        try {
            // Captcha verilerini kontrol et ve gerekirse oluştur
            long captchaCount = captchaRepository.count();
            if (captchaCount == 0) {
                System.out.println("Veritabanında captcha bulunamadı, başlangıç captcha verileri oluşturuluyor...");
                captchaGeneratorService.generateAndSaveCaptchas(20);
                System.out.println("20 adet captcha başarıyla oluşturuldu.");
            } else {
                System.out.println("Veritabanında " + captchaCount + " adet captcha bulundu.");
            }

            // User verilerini kontrol et ve gerekirse oluştur
            long userCount = userRepository.count();
            if (userCount == 0) {
                System.out.println("Veritabanında kullanıcı bulunamadı, varsayılan kullanıcı oluşturuluyor...");
                userVerificationService.registerUser("admin", "password123");
                System.out.println("Varsayılan kullanıcı oluşturuldu: admin/password123");
            } else {
                System.out.println("Veritabanında " + userCount + " adet kullanıcı bulundu.");
            }

        } catch (Exception e) {
            System.err.println("Başlangıç verilerini oluştururken hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
