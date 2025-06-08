package tr.edu.duzce.mf.bm.bm470captcha.exception;

/**
 * Kullanıcı işlemleri sırasında oluşabilecek hatalar için özel Exception sınıfı.
 */
public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException() {
        super("Kullanıcı işlemi sırasında bir hata oluştu.");
    }
}
