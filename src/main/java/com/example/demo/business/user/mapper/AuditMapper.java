package com.example.demo.business.user.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.user.entity.Audit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/21 21:25
 */
@Mapper
@Repository
public interface AuditMapper{

    int save(Audit audit);

    List<Audit> list(String userId);

    int remove(String id);

    int update(Audit audit);

    List<Audit> getAuditList(String id, String type);
}
