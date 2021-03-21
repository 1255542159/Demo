package com.example.demo.business.user.entity;

import com.example.demo.base.BaseEntity;
import lombok.Data;

/**
 * 请假和入团审核表
 * @author Joy
 */
@Data
public class Audit extends BaseEntity {

  private String type;
  private String description;
  private Integer leaveTime;
  private Integer status;
  private String userId;
  private String clubId;
}
