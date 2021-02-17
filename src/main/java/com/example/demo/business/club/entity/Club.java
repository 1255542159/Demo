package com.example.demo.business.club.entity;

import com.example.demo.base.BaseEntity;
import lombok.Data;


/**
 * @author Joy
 */
@Data
public class Club extends BaseEntity {

  private String clubName;
  private String clubCreator;
  private long leaderId;
  private String clubIntroduce;
  private String clubDesc;
  private String clubImg;
  private long isDelete;
}
