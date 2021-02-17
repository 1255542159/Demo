package com.example.demo.business.user.entity;

import com.example.demo.base.BaseEntity;
import lombok.Data;

/**
 * @author Joy
 */
@Data
public class RoleMenu extends BaseEntity {

  private long roleId;
  private long menuId;
}
