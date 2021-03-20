package com.example.demo.business.admin.service;


import com.example.demo.base.ResponseVo;

/**
 * @author joy
 * @version 1.0
 * @date 2020/10/3 16:56
 */

public interface AdminService {

    ResponseVo getUserCount();

    ResponseVo getClubCount();

    ResponseVo roleList();

    ResponseVo powerList(String id);
}
