package tr.edu.duzce.mf.bm.bm470captcha.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // Parametreleri doğrudan HttpServletRequest içinden al (request.getParameterMap)
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, Object> paramLog = new HashMap<>();

        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            paramLog.put(entry.getKey(), Arrays.toString(entry.getValue()));
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("uri", request.getRequestURI());
        logMap.put("httpMethod", request.getMethod());
        logMap.put("controller", handlerMethod.getBeanType().getSimpleName());
        logMap.put("method", method.getName());
        logMap.put("parameters", paramLog);

        // Accept-Language gibi header bilgilerini de ekleyebilirsin:
        String localeHeader = request.getHeader("Accept-Language");
        if (localeHeader != null) {
            logMap.put("Accept-Language", localeHeader);
        }

        logger.info("API Request -> {}", logMap);
        System.out.println(">>> Interceptor çalıştı: " + logMap);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView) {
        logger.debug("Completed processing of URI: {}", request.getRequestURI());
    }
}
