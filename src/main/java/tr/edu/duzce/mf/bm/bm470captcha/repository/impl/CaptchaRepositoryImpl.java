package tr.edu.duzce.mf.bm.bm470captcha.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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

            return entityManager.createQuery(cq)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new CaptchaException("Veritabanında kayıtlı Captcha bulunamadı.");
        } catch (Exception e) {
            throw new CaptchaException("Rastgele Captcha getirirken hata oluştu.", e);
        }
    }

    @Override
    public List<Captcha> findAll(int page, int size) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Captcha> cq = cb.createQuery(Captcha.class);
            Root<Captcha> root = cq.from(Captcha.class);
            cq.select(root);

            return entityManager.createQuery(cq)
                    .setFirstResult(page * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (Exception e) {
            throw new CaptchaException("Tüm Captchaları getirirken hata oluştu.", e);
        }
    }
    @Override
    public long count() {
        Query query = entityManager.createQuery("SELECT COUNT(c) FROM Captcha c");
        return (Long) query.getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        try {
            Captcha captcha = entityManager.find(Captcha.class, id);
            if (captcha != null) {
                entityManager.remove(captcha);
            } else {
                throw new CaptchaException("Belirtilen ID ile Captcha bulunamadı.");
            }
        } catch (Exception e) {
            throw new CaptchaException("Captcha silinirken hata oluştu.", e);
        }
    }
}
