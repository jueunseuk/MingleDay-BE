package returns.mingleday.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import returns.mingleday.domain.users.User;
import returns.mingleday.global.constant.SecurityConstant;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String header = request.getHeader("Authorization");

        String token = "";
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        } else {
            log.debug("Authorization header is missing");
        }

        if (token != null && jwtTokenProvider.isValidToken(token)) {
            User currentUser = jwtTokenProvider.getUser(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            currentUser.getUserId(), null, null
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        try {
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return SecurityConstant.PERMIT_ENDPOINTS.contains(path);
    }
}