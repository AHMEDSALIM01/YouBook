package com.youbook.YouBook.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youbook.YouBook.handler.CustomAuthenticationFailureHandler;
import com.youbook.YouBook.security.models.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private CustomAuthenticationFailureHandler failureHandler;
    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomAuthenticationFailureHandler failureHandler){
        this.authenticationManager = authenticationManager;
        this.failureHandler=failureHandler;
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            try {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email,password);
                return authenticationManager.authenticate(authenticationToken);
            }catch (BadCredentialsException e){
                throw new BadCredentialsException(e.getMessage());
            }catch (DisabledException e){
                throw new DisabledException(e.getMessage());
            }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successAuthentication");
        MyUser user = (MyUser) authResult.getPrincipal();
        Algorithm algorithm= Algorithm.HMAC256("monSecret926600");
        String jwtAccessToken = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+2*60*1000))
                .withClaim("roles",user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                .withClaim("user_id",user.getId())
                .withClaim("enabled",user.getIs_active())
                .withClaim("user_name",user.getName())
                .sign(algorithm);
        response.setHeader("Authorization",jwtAccessToken);
        String jwtRefreshToken = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
                .sign(algorithm);
        Map<String,String> idToken = new HashMap<>();
        idToken.put("accessToken",jwtAccessToken);
        idToken.put("refreshToken",jwtRefreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(),idToken);
    }
}
