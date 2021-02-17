package com.example.demo.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 17:01
 */
public class BaseServiceImpl<T> implements BaseService<T> {
    

    private BaseMapper baseMapper;

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public int save(T entity) {
        return baseMapper.save(entity);
    }

    @Override
    public int remove(T entity) {
        return baseMapper.remove(entity);
    }

    @Override
    public int update(T entity) {
        return baseMapper.update(entity);
    }

    @Override
    public T findById(Serializable id) {
        return (T) baseMapper.findById(id);
    }

    @Override
    public List<T> findAll() {
        return baseMapper.findAll();
    }
}
