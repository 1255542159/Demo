package com.example.demo.business.user.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Joy
 */
@Data
@ToString
public class User implements UserDetails {

    private String id;
    @ApiModelProperty(notes = "学号")
    private String sno;
    @ApiModelProperty(notes = "密码")
    private String password;
    @ApiModelProperty(notes = "姓名")
    private String name;
    @ApiModelProperty(notes = "性别")
    private String sex;
    @ApiModelProperty(notes = "头像")
    private String avatar;
    @ApiModelProperty(notes = "手机号")
    private String phone;
    @ApiModelProperty(notes = "邮箱")
    private String email;
    @ApiModelProperty(notes = "签名")
    private String sign;
    @ApiModelProperty(notes = "状态：0表示删除，1表示正常")
    private Integer status;
    @ApiModelProperty(notes = "注册IP")
    private String regIp;
    @ApiModelProperty(notes = "登录IP")
    private String loginIp;
    @ApiModelProperty(notes = "社团ID")
    private String clubId;
    @ApiModelProperty(notes = "负责人ID")
    private String parentId;
    @ApiModelProperty(notes = "创建时间")
    private Date createTime;
    @ApiModelProperty(notes = "更新时间")
    private Date updateTime;
    @ApiModelProperty(notes = "角色")
    private Role roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(roles.getRoleName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
