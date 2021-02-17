package com.example.demo.business.club.contorller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.club.service.ClubService;
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
     * 社团列表
     * @return
     */
    @GetMapping("/clubList")
    public ResponseVo getClubList(@RequestParam(value = "page", defaultValue = "1",required = false) int page,
                                  @RequestParam(value = "size", defaultValue = "5",required = false) int size,
                                  @RequestParam(value = "keyWords",required = false) String keyWords ){
        return clubService.getClubList(page,size,keyWords);
    }
}
