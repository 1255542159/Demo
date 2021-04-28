package com.example.demo.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:51
 */
@Data
public class BaseEntity implements Serializable {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;
}
