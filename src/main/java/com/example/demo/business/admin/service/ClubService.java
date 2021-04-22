package com.example.demo.business.admin.service;


import com.example.demo.base.BaseService;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.user.entity.Audit;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:42
 */
public interface ClubService extends BaseService<Club> {


    ResponseVo list();


    ResponseVo getAuditList(int page, int size, String type);

    ResponseVo agreeAudit(Audit audit);
}
