package tr.edu.duzce.mf.bm.bm470captcha.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import tr.edu.duzce.mf.bm.bm470captcha.interceptor.RequestInterceptor;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"tr.edu.duzce.mf.bm.bm470captcha"})
public class WebConfig implements WebMvcConfigurer {

    // JSP view resolver
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }

    // Locale resolver (çerez temelli)
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.forLanguageTag("tr-TR"));
        clr.setCookieName("bm470-captcha");
        clr.setCookieMaxAge(3600); // saniye
        return clr;
    }

    // Lang parametresi ile dil değiştirme desteği
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // ?lang=en gibi kullanım
        return lci;
    }

    // Interceptor kayıtları
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/*");
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/*");
    }

    // Mesaj kaynakları
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setFallbackToSystemLocale(false);
        return ms;
    }
}
