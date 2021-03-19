package com.example.demo.business.portal.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/19 13:31
 */
@Mapper
@Repository
public interface PortalMapper {

    List<HashMap<String,Object>> getActivityList(String clubId, String sponsorId, int status);

    HashMap<String, Object> getDetails(String id);
}
