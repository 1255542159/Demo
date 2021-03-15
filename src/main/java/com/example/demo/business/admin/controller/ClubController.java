package com.example.demo.business.admin.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.admin.service.ClubService;
import com.example.demo.business.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:40
 */
@RestController
@RequestMapping("/club")
public class ClubController {

    @Autowired
    private ClubService clubService;
    /**
     * 添加社团
     *
     * @return
     */
    @PostMapping("/addClub")
    public ResponseVo addClub(@RequestBody Club club) {
        return clubService.save(club);
    }

    /**
     * 社团列表分页
     * @return
     */
    @GetMapping("/clubList/{page}/{size}")
    public ResponseVo getClubList(@PathVariable(value = "page",required = false) int page,
                                  @PathVariable(value = "size", required = false) int size,
                                  @RequestParam(value = "status",required = false) Integer status,
                                  @RequestParam(value = "keyWords",required = false) String keyWords ){
        return clubService.getList(page,size,status,keyWords);
    }

    /**
     * 获取社团列表不分页
     * @return
     */
    @GetMapping("/list/club")
    public ResponseVo list(){
        return clubService.list();
    }

    /**
     * 通过社团id删除社团
     *
     * @param clubId
     * @return
     */
    @DeleteMapping("/deleteClub/{clubId}")
    public ResponseVo deleteClub(@PathVariable("clubId") String clubId) {
        return clubService.remove(clubId);
    }

    /**
     * 根据社团id更新社团
     * @param club
     * @return
     */
    @PostMapping("/updateClub")
    public ResponseVo updateClub(@RequestBody Club club) {
        return clubService.update(club);
    }



}
