package com.example.demo.business.club.entity;

import lombok.Data;

@Data
public class Activity {


  private String activityName;
  private String activityDes;
  private String activityContent;
  private java.sql.Timestamp startTime;
  private java.sql.Timestamp endTime;
  private String activityCreator;
  private String activityImg;
  private long isDelete;

}
