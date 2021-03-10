package com.example.demo.business.activity.entity;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(notes = "发起人ID")
    private String sponsorId;
    @ApiModelProperty(notes = "活动首图")
    private String activityImg;
    @ApiModelProperty(notes = "是否删除 0：正常1：停用")
    private long isDelete;
    @ApiModelProperty(notes = "状态0：未审核，1：已审核，2：审核中")
    private Integer status;

}
