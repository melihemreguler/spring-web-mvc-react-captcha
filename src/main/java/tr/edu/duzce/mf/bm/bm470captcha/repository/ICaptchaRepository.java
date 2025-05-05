package tr.edu.duzce.mf.bm.bm470captcha.repository;


import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;

public interface ICaptchaRepository {
    Captcha findById(Long id);
    void save(Captcha captcha);
    Captcha findRandomCaptcha();
}