package tr.edu.duzce.mf.bm.bm470captcha.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tr.edu.duzce.mf.bm.bm470captcha.entity.Captcha;
import tr.edu.duzce.mf.bm.bm470captcha.entity.User;

import java.util.Properties;

import static org.hibernate.cfg.Environment.*;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:hibernate.properties", encoding = "UTF-8")
@ComponentScan(basePackages = "tr.edu.duzce.mf.bm.bm470captcha")  // tüm alt paketleri tarar
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        Properties props = new Properties();
        props.put(DRIVER, env.getProperty("postgresql.driver"));
        props.put(URL, env.getProperty("postgresql.url"));
        props.put(USER, env.getProperty("postgresql.user"));
        props.put(PASS, env.getProperty("postgresql.password"));
        props.put(DIALECT, env.getProperty("hibernate.dialect"));
        props.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
        props.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
        props.put(DEFAULT_SCHEMA, env.getProperty("hibernate.default_schema"));

        // C3P0 settings
        props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
        props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
        props.put(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
        props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
        props.put(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));
        props.put("hibernate.c3p0.initialPoolSize", env.getProperty("hibernate.c3p0.initialPoolSize"));

        factoryBean.setHibernateProperties(props);

        // Entity sınıfların
        factoryBean.setAnnotatedClasses(
               Captcha.class,
                User.class
        );

        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}