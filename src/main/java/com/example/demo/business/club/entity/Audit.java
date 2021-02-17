package com.example.demo.business.club.entity;

import lombok.Data;

@Data
public class Audit {


  private String type;
  private String description;
  private java.sql.Timestamp leaveTime;
  private String status;
  private long userId;


}
