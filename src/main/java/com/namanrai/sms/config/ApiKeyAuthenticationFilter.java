package com.namanrai.sms.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final String configuredApiKey;

    public ApiKeyAuthenticationFilter(
            @Value("${app.security.api-key}") String configuredApiKey) {
        this.configuredApiKey = configuredApiKey;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (HttpMethod.GET.matches(request.getMethod())
                || HttpMethod.OPTIONS.matches(request.getMethod())
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String suppliedApiKey = request.getHeader(API_KEY_HEADER);

        if (configuredApiKey != null
                && !configuredApiKey.isBlank()
                && configuredApiKey.equals(suppliedApiKey)) {

            var authentication = new UsernamePasswordAuthenticationToken(
                    "api-key-client",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_API")
            );

            org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Valid X-API-KEY header is required for write operations"
        );
    }
}
