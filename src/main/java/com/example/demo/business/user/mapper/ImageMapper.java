package com.example.demo.business.user.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.user.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/11 14:05
 */
@Mapper
@Repository
public interface ImageMapper extends BaseMapper<Image> {
    /**
     * 保存图片
     * @param entity
     * @return
     */
    @Override
    int save(Image entity);
}
