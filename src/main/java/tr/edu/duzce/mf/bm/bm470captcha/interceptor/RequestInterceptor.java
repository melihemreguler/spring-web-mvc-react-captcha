package tr.edu.duzce.mf.bm.bm470captcha.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        Parameter[] parameters = method.getParameters();

        // Parametre bilgilerini veri tipi ve değeri ile al
        Map<String, Object> paramMap = new HashMap<>();
        for (Parameter param : parameters) {
            String name = param.getName(); // Parametre ismi
            String value = request.getParameter(name); // Değer (request parametrelerinden)
            paramMap.put(name + ":" + param.getType().getSimpleName(), value);
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("uri", request.getRequestURI());
        logMap.put("httpMethod", request.getMethod());
        logMap.put("controller", handlerMethod.getBeanType().getSimpleName());
        logMap.put("method", method.getName());
        logMap.put("parameters", paramMap);

        logger.info("API Request -> {}", logMap);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView) {
        // İsteğin sonrasında yapılacak ek loglama burada olabilir (örneğin statusCode)
        logger.debug("Completed processing of URI: {}", request.getRequestURI());
    }
}
