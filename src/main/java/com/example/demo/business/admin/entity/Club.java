package com.example.demo.business.admin.entity;

import com.example.demo.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author Joy
 */
@Data
public class Club extends BaseEntity {

    @ApiModelProperty(notes = "社团名称")
    private String clubName;
    @ApiModelProperty(notes = "社团创建者")
    private String clubCreator;
    @ApiModelProperty(notes = "社团负责人")
    private String leaderId;
    @ApiModelProperty(notes = "社团介绍")
    private String clubIntroduce;
    @ApiModelProperty(notes = "社团描述")
    private String clubDesc;
    @ApiModelProperty(notes = "社团大图")
    private String clubImg;
    @ApiModelProperty(notes = "是否启用")
    private Integer isDelete;
    @ApiModelProperty(notes = "社团人数")
    private Integer amount;
    @ApiModelProperty(notes = "状态1表示已通过，2审核中，3已拒绝")
    private Integer status;
}
