package com.example.demo.business.admin.entity;

import com.example.demo.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Joy
 */
@Data
public class Activity extends BaseEntity {

    @ApiModelProperty(notes = "活动标题")
    private String title;
    @ApiModelProperty(notes = "活动摘要")
    private String summary;
    @ApiModelProperty(notes = "活动内容")
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    @ApiModelProperty(notes = "发起人ID")
    private String sponsorId;
    @ApiModelProperty(notes = "活动首图")
    private String activityImg;
    @ApiModelProperty(notes = "是否删除 0：正常1：停用")
    private Integer isDelete;
    @ApiModelProperty(notes = "0表示全部，1表示已通过，2审核中，3已拒绝")
    private Integer status;
    @ApiModelProperty(notes = "社团ID")
    private String clubId;
    @ApiModelProperty(notes = "参数数")
    private Integer viewCount;
    @ApiModelProperty(notes = "参与人数")
    private Integer joinCount;
}
