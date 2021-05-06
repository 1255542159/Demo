package com.example.demo.business.admin.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.admin.entity.Settings;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author joy
 * @version 1.0
 * @date 2021/5/7 1:09
 */
@Repository
@Mapper
public interface SettingsMapper extends BaseMapper<Settings> {

    @Override
    int save(Settings settings);

    @Override
    int update(Settings entity);

    Settings findSettingsByKey(String key);
}
