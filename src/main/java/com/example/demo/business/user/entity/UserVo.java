package com.example.demo.business.user.entity;


import com.example.demo.business.admin.entity.Club;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author joy
 * @version 1.0
 * @date 2021/2/28 17:15
 */
@Data
@ToString
public class UserVo {
    private String id;
    @ApiModelProperty(notes = "昵称")
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
    private Integer clubId;
    @ApiModelProperty(notes = "社团")
    private Club club;
    @ApiModelProperty(notes = "负责人ID")
    private Integer parentId;
    @ApiModelProperty(notes = "创建时间")
    private Date createTime;
    @ApiModelProperty(notes = "更新时间")
    private Date updateTime;

}
