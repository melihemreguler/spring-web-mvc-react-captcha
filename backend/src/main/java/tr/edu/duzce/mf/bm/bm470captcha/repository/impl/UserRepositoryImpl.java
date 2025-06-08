package tr.edu.duzce.mf.bm.bm470captcha.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.bm470captcha.entity.User;
import tr.edu.duzce.mf.bm.bm470captcha.repository.IUserRepository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements IUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByUsername(String username) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);

            Predicate predicate = cb.equal(root.get("username"), username);
            cq.select(root).where(predicate);

            List<User> result = entityManager.createQuery(cq).getResultList();
            return result.isEmpty() ? null : result.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Kullanıcı sorgulama sırasında hata oluştu", e);
        }
    }

    @Override
    public void save(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            throw new RuntimeException("Kullanıcı kaydetme sırasında hata oluştu", e);
        }
    }
}
