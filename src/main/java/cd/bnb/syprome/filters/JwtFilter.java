package cd.bnb.syprome.filters;

import cd.bnb.syprome.services.UserJwtService;
import cd.bnb.syprome.utilities.JwtUtility;
import cd.bnb.syprome.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




@Component
@AllArgsConstructor
@Slf4j

public class JwtFilter extends OncePerRequestFilter
{

   private JwtUtility jwtUtility;

   private UserJwtService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {


        final String authorization = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            username =jwtUtility.getUsernameFromToken(token);
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetails = userService.loadUserByUsername(username);
            Utils.users=userService.getUser(username);
            if (jwtUtility.validateToken(token, userDetails)) {
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }



        filterChain.doFilter(request,response);
    }

}