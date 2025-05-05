package tr.edu.duzce.mf.bm.bm470captcha.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig {

    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            new Locale("tr"),
            new Locale("en")
    );

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");
                if (headerLang == null || headerLang.isEmpty()) {
                    return Locale.forLanguageTag("tr");
                }

                List<Locale.LanguageRange> list = Locale.LanguageRange.parse(headerLang);
                Locale locale = Locale.lookup(list, SUPPORTED_LOCALES);
                return locale != null ? locale : Locale.forLanguageTag("tr");
            }
        };
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setDefaultEncoding("UTF-8"); // Türkçe karakter problemi çözülür
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
