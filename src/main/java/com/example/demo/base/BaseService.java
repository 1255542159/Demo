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
    ResponseVo save(T entity);

    /**
     * 删除
     * @param id
     * @return
     */
    ResponseVo remove(Integer id);

    /**
     * 更新
     * @param entity
     * @return
     */
    ResponseVo update(T entity);

    /**
     * 查询所有
     * @param page
     * @param size
     * @param keyWords
     * @return
     */
    ResponseVo getList(int page,int size, String keyWords);


}
