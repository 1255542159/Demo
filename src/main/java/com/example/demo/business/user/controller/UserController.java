package com.example.demo.business.user.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.user.entity.Image;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.service.UserService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 13:41
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseVo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        user.setPassword("");
        return ResponseVo.SUCCESS().setData(user);
    }

    /**
     * 根据当前角色去获取对应菜单
     *
     * @return
     */
    @GetMapping("/getMenu")
    public ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response) {
        return userService.getMenuList(request, response);
    }

    /**
     * 用户头像上传
     *
     * @param file
     * @return
     */
    @PostMapping("image/uploadImage")
    public ResponseVo uploadImage(@RequestParam("file") MultipartFile file,
    @RequestParam("original") String original) {
        return userService.uploadImage(file,original);
    }

    /**
     * 获取图片列表
     * @param page
     * @param size
     * @param original
     * @return
     */
    @GetMapping("/image/list")
    public ResponseVo getImageList(@RequestParam("page") int page,
                                  @RequestParam("size") int size,
                                  @RequestParam(value = "original",required = false) String original) {
        return userService.getImageList(page,size,original);
    }

    /**
     * 人员列表
     *
     * @return
     */
    @GetMapping("/personnelList")
    public ResponseVo personnelList(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    @RequestParam(value = "keyWords", required = false) String keyWords) {
        return userService.getList(page, size, keyWords);
    }

    /**
     * 添加人员
     *
     * @return
     */
    @PostMapping("/addPersonnel")
    public ResponseVo addPersonnel(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据人员id更新人员
     *
     * @param user
     * @return
     */
    @PutMapping("/updatePersonnel")
    public ResponseVo updatePersonnel(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 根据用户id删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/deletePersonnel/{userId}")
    public ResponseVo deletePersonnel(@PathVariable("userId") Integer userId){
        return userService.remove(userId);
    }

}
