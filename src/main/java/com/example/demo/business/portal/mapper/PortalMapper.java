package com.example.demo.business.portal.mapper;
import com.example.demo.business.user.entity.UserVo;
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

    List<HashMap<String, Object>> getClubList(String clubId, String sponsorId, int i);

    HashMap<String, Object> getClubDetail(String id);

    UserVo getActivityUserInfo(String id);
}
