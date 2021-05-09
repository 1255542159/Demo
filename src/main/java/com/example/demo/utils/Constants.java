package com.example.demo.utils;

import io.swagger.models.auth.In;

/**
 * @author joy
 * @version 1.0
 * @date 2021/2/28 16:48
 */
public interface Constants {
    interface DEFAULT {
        String AVATAR = "https://imgs.sunofbeaches.com/group1/M00/00/3A/rBsADV_PbTyAJexxAABHFzLgPUo164.png";
    }

    interface Page {
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 5;
    }

    /**
     * 角色
     */
    interface Role {
        String ROLE_ADMIN = "ROLE_ADMIN";
        String ROLE_CLUB = "ROLE_CLUB";
        String ROLE_USER = "ROLE_USER";
    }


    interface RoleId {
        String ROLE_ADMIN = "1";
        String ROLE_CLUB = "2";
        String ROLE_USER = "3";
    }


    /**
     * 活动状态
     */
    interface ActivityStatus {
        //0表示全部，1表示已通过，2审核中，3已拒绝
        int TO_AUDIT = 2; //审核中
        int PASSED = 1;   //审核通过
        int UNAPPROVE = 3;//审核未通过
    }

    /**
     * 图片类型
     */
    interface ImageOriginal {
        String ACTIVITY = "activity";
        String AVATAR = "avatar";
        String CLUB = "club";
    }
}
