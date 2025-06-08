package tr.edu.duzce.mf.bm.bm470captcha.repository;

import tr.edu.duzce.mf.bm.bm470captcha.entity.User;

public interface IUserRepository {
    User findByUsername(String username);
    void save(User user);
}
