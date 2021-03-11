package com.example.demo.business.user.entity;


import com.example.demo.base.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Joy
 */
@Data
public class Menu extends BaseEntity {

  private String url;
  private String path;
  private String component;
  private String name;
  private String icon;
  private Integer keepAlive;
  private Integer requireAuth;
  private String parentId;
  private Integer menuLevel;
  private List<Menu> childMenu;
}
