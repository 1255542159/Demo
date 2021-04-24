package com.example.demo.base;

import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 17:02
 */
@Mapper
public interface BaseMapper<T> {

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(T entity);

    /**
     * 删除
     * @param id
     * @return
     */
    int remove(String id);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(T entity);


    /**
     * 查询所有
     * @returnpersonnelList
     */
    List<T> findAll();
}
