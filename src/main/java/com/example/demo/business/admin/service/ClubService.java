package com.example.demo.business.admin.service;


import com.example.demo.base.BaseService;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:42
 */
public interface ClubService extends BaseService<Club> {


    ResponseVo list();


}
