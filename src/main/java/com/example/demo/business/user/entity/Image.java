package com.example.demo.business.user.entity;

import com.example.demo.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Joy
 */
@Data
public class Image extends BaseEntity {
    @ApiModelProperty(notes = "用户ID")
    private String userId;
    @ApiModelProperty(notes = "图片路径")
    private String url;
    @ApiModelProperty(notes = "状态0：正常，1：停用")
    private Integer status;
    @ApiModelProperty(notes = "图片类型")
    private String original;
}
