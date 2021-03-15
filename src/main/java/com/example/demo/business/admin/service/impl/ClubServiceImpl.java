package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.admin.mapper.ClubMapper;
import com.example.demo.business.admin.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:53
 */
@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Override
    public ResponseVo getList(int page, int size, int status, String keyWords) {
            PageHelper.startPage(page, size);
            List<Club> all = clubMapper.findAll();
            PageInfo<Club> clubPageInfo = new PageInfo<>(all);
            return ResponseVo.SUCCESS().setData(clubPageInfo);
    }

    @Override
    public ResponseVo save(Club entity) {
        entity.setCreateTime(new Date());
        int save = clubMapper.save(entity);
        if (save != 1) {
            return ResponseVo.FAILURE().setMsg("保存失败");
        }
        return ResponseVo.SUCCESS().setMsg("保存成功");
    }

    @Override
    public ResponseVo remove(String id) {
        int remove = clubMapper.remove(id);
        if (remove != 1) {
            return ResponseVo.FAILURE().setMsg("删除失败");
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(Club entity) {
        entity.setUpdateTime(new Date());
        int update = clubMapper.update(entity);
        if (update != 1) {
            return ResponseVo.FAILURE().setMsg("更新失败");
        }
        return ResponseVo.SUCCESS().setMsg("更新成功");
    }

    @Override
    public ResponseVo list() {
        List<Club> all = clubMapper.findAll();
        return ResponseVo.SUCCESS().setData(all);

    }
}
