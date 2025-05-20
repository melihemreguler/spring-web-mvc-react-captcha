package tr.edu.duzce.mf.bm.bm470captcha.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.exception.CaptchaException;
import tr.edu.duzce.mf.bm.bm470captcha.repository.ICaptchaRepository;

import java.util.List;
import java.util.Random;

@Repository
public class CaptchaRepositoryImpl implements ICaptchaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final Random random = new Random();

    @Override
    public Captcha findById(Long id) {
        try {
            return entityManager.find(Captcha.class, id);
        } catch (Exception e) {
            throw new CaptchaException("Captcha arama sırasında hata oluştu.", e);
        }
    }

    @Override
    public void save(Captcha captcha) {
        try {
            entityManager.persist(captcha);
        } catch (Exception e) {
            throw new CaptchaException("Captcha kaydetme sırasında hata oluştu.", e);
        }
    }

    @Override
    public Captcha findRandomCaptcha() {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Captcha> cq = cb.createQuery(Captcha.class);
            Root<Captcha> root = cq.from(Captcha.class);
            cq.select(root);

            // PostgreSQL'de rastgele sıralama: ORDER BY RANDOM()
            cq.orderBy(cb.asc(cb.function("RANDOM", Double.class)));

            Captcha captcha = entityManager.createQuery(cq)
                    .setMaxResults(1)
                    .getSingleResult();

            return captcha;
        } catch (NoResultException e) {
            throw new CaptchaException("Veritabanında kayıtlı Captcha bulunamadı.");
        } catch (Exception e) {
            throw new CaptchaException("Rastgele Captcha getirirken hata oluştu.", e);
        }
    }


    /*public Captcha findRandomCaptcha() {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Captcha> cq = cb.createQuery(Captcha.class);
            Root<Captcha> root = cq.from(Captcha.class);
            cq.select(root);

            List<Captcha> captchas = entityManager.createQuery(cq).getResultList();

            if (captchas.isEmpty()) {
                throw new CaptchaException("Veritabanında kayıtlı Captcha bulunamadı.");
            }

            int randomIndex = random.nextInt(captchas.size());
            return captchas.get(randomIndex);
        } catch (Exception e) {
            throw new CaptchaException("Rastgele Captcha getirirken hata oluştu.", e);
        }
    }*/
}
