package returns.mingleday.util;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import returns.mingleday.service.user.MingleDayUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MingleDayUserDetailsService mingleDayUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (StringUtils.hasText(token)) {
            try {
                if (jwtTokenProvider.isValidToken(token)) {
                    String userId = jwtTokenProvider.getUserId(token);
                    UserDetails userDetails = mingleDayUserDetailsService.loadUserByUsername(userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("[JWT-Filter] Success to Authentication - userId:{}, email:{}", userId, userDetails.getUsername());
                }
            } catch (ExpiredJwtException e) {
                log.warn("[JWT-Filter] Token Expired");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("""
            {
              "code": "EXPIRED_TOKEN",
              "message": "토큰 만료"
            }
        """);
                return;

            } catch (Exception e) {
                log.error("[JWT-Filter] Invalid Token");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("""
            {
              "code": "INVALID_TOKEN",
              "message": "유효하지 않은 토큰"
            }
        """);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
