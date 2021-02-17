package com.example.demo.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:43
 */
public interface BaseService<T> {

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(T entity);

    /**
     * 删除
     * @param entity
     * @return
     */
    int remove(T entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    T findById(Serializable id);

    /**
     * 查询所有
     * @return
     */
    List<T> findAll();
}
