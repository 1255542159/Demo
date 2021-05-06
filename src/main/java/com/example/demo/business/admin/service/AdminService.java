package com.example.demo.business.admin.service;


import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Settings;
import com.example.demo.business.user.entity.Image;

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

    ResponseVo manageRole(String roleId, String idStr);

    ResponseVo getList(int page, int size, Integer status, String keyWords);

    ResponseVo issueRole(String userId, String roleId);

    ResponseVo updateCarousel(Image image);

    ResponseVo deleteCarousel(String id);

    ResponseVo getActivityCount();

    ResponseVo loginLog();

    ResponseVo saveAbout(Settings settings);

    ResponseVo getAbout();
}
