package tr.edu.duzce.mf.bm.bm470captcha.repository;


import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;

public interface ICaptchaGeneratorRepository {
    void save(Captcha captcha);
}
