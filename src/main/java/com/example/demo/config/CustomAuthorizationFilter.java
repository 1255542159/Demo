package com.example.demo.config;

import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.service.impl.UserServiceImpl;
import com.example.demo.utils.JwtTokenUtils;
import com.example.demo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权
 * 对每一次请求进行鉴权
 *
 * @author joy
 * @version 1.0
 * @date 2020/12/28 18:19
 */

public class CustomAuthorizationFilter extends OncePerRequestFilter {



    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = JwtTokenUtils.getToken(request);
        if (token == null || JwtTokenUtils.isTokenExpired(token) == true) {
            chain.doFilter(request, response);
            return;
        }
        String username = JwtTokenUtils.getUsername(token);
        if (username != null && !"".equals(username)) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (userDetails != null && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                //设置当前登录用户到上下文
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } else {
                //用户不合法，清除上下文
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

}
