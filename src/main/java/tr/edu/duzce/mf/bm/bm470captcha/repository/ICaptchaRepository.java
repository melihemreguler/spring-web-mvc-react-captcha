package tr.edu.duzce.mf.bm.bm470captcha.repository;


import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;

import java.util.List;

public interface ICaptchaRepository {
    Captcha findById(Long id);
    void save(Captcha captcha);
    Captcha findRandomCaptcha();
    List<Captcha> findAll(int page, int size);
    void deleteById(Long id);
    long count();
}