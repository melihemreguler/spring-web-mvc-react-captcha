package tr.edu.duzce.mf.bm.bm470captcha.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        String responseBody = responseWrapper.getCapturedResponseBody();

        logger.info("Response Body -> {}", responseBody);
        System.out.println(">>> Response içeriği: " + responseBody);

        // Gerçek cevabı tekrar yaz
        response.getOutputStream().write(responseBody.getBytes());
    }
}
