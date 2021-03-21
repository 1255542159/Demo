package com.example.demo.config;

import com.example.demo.business.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 11:39
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public CustomAuthorizationFilter jwtTokenFilter(){
        return new CustomAuthorizationFilter();
    }

    /**
     * 角色继承
     *
     * @return
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_CLUB > ROLE_USER");
        return roleHierarchy;
    }

    /**
     * 配置数据源
     * 对密码加密
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 对静态资源放行
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }

    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT，我们这里不需要csrf
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 登录URL
                .antMatchers("/login").permitAll()
                .antMatchers("/portal/**").permitAll()
                .antMatchers("/user/captcha", "/user/check-token", "/user/uploadImage").permitAll()
                .antMatchers("/admin/web_site_info/**").permitAll()
                .antMatchers("/swagger**/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/club/**").hasRole("CLUB")
//                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http.logout().logoutUrl("/logout").logoutSuccessHandler(new CustomLogoutSuccessHandler());
        http.addFilter(new CustomAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(jwtTokenFilter(), BasicAuthenticationFilter.class);
    }
}
