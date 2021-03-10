package com.example.demo.business.club.contorller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.club.entity.Club;
import com.example.demo.business.club.service.ClubService;
import com.example.demo.business.user.entity.User;
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

    @Autowired
    private UserService userService;

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
     * 社团列表
     * @return
     */
    @GetMapping("/clubList")
    public ResponseVo getClubList(@RequestParam(value = "page", defaultValue = "1",required = false) int page,
                                  @RequestParam(value = "size", defaultValue = "5",required = false) int size,
                                  @RequestParam(value = "keyWords",required = false) String keyWords ){
        return clubService.getList(page,size,keyWords);
    }

    /**
     * 通过社团id删除社团
     *
     * @param clubId
     * @return
     */
    @DeleteMapping("/deleteClub/{clubId}")
    public ResponseVo deleteClub(@PathVariable("clubId") Integer clubId) {
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
