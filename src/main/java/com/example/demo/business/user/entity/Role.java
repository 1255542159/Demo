package com.example.demo.business.user.entity;

import com.example.demo.base.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Joy
 */
@Data
public class Role extends BaseEntity {

  private String roleName;
  private String roleDesc;
  private Integer status;
  private Integer isDelete;
  private List<Menu> childMenu;
}
