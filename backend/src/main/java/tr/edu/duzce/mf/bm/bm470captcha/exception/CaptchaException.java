package tr.edu.duzce.mf.bm.bm470captcha.exception;

/**
 * Captcha işlemleri sırasında oluşabilecek hatalar için özel Exception sınıfı.
 */
public class CaptchaException extends RuntimeException {

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }

    public CaptchaException() {
        super("Captcha işlemi sırasında bir hata oluştu.");
    }
}
