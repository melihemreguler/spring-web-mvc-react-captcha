package tr.edu.duzce.mf.bm.bm470captcha.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.bm470captcha.entity.User;
import tr.edu.duzce.mf.bm.bm470captcha.exception.UserException;
import tr.edu.duzce.mf.bm.bm470captcha.repository.IUserRepository;

@Service
@Transactional
public class UserVerificationService {

    private final IUserRepository userRepository;

    @Autowired
    public UserVerificationService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean verifyLogin(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UserException("Kullanıcı adı bulunamadı.");
            }

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new UserException("Yanlış şifre.");
            }

            return true;
        } catch (Exception e) {
            throw new UserException("Kullanıcı doğrulama işlemi sırasında bir hata oluştu.", e);
        }
    }

    public void registerUser(String username, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);

            userRepository.save(user);
        } catch (Exception e) {
            throw new UserException("Kullanıcı kaydetme işlemi sırasında bir hata oluştu.", e);
        }
    }
}
