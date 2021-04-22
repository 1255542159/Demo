package com.example.demo.config;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.service.impl.UserServiceImpl;
import com.example.demo.utils.JwtTokenUtils;
import com.example.demo.utils.Tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * 用户验证过滤器
 *
 * @author joy
 * @version 1.0
 * @date 2020/12/28 17:23
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;

    private static UserServiceImpl userService;

//    static {
//        DemoApplication.configurableApplicationContext.getBean(UserServiceImpl.class);
//    }

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.manager = authenticationManager;
    }


    /**
     * 从request获取用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();
        //将用户信息的校验交给UsernamePasswordAuthenticationToken去做
        return manager.authenticate(new UsernamePasswordAuthenticationToken(username,
                password, new ArrayList<>()));
    }


    /**
     * 验证成功后调用
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //将用户当前登录的ip持久化到数据库
        ServletContext context = request.getServletContext();
        WebApplicationContext ctx;
        ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        userService = ctx.getBean(UserServiceImpl.class);
        User user1 = (User) authResult.getPrincipal();
        user1.setLoginIp(Tools.getIpAddr(request));
        userService.update(user1);
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        User user = (User) authResult.getPrincipal();
        System.out.println("jwtUser:" + user.toString());
        String token = JwtTokenUtils.createToken(user.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        response.setHeader("cms_token", token);
        user.setPassword(null);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(ResponseVo.SUCCESS().setData(user).setMsg("登录成功")));
        out.flush();
        out.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(ResponseVo.FAILURE().setMsg("账户或密码错误!")));
        out.flush();
        out.close();
    }
}
