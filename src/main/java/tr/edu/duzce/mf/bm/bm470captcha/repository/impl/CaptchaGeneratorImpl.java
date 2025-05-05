package tr.edu.duzce.mf.bm.bm470captcha.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaGeneratorRepository;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;

@Repository
public class CaptchaGeneratorImpl implements ICaptchaGeneratorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Captcha captcha) {
        try {
            entityManager.persist(captcha);
        } catch (Exception e) {
            throw new RuntimeException("Captcha kaydedilemedi.", e);
        }
    }
}
