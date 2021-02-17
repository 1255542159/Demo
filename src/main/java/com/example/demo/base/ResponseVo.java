package com.example.demo.base;

import com.sun.org.apache.regexp.internal.RE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 16:04
 */
@Data
public class ResponseVo{

    private Integer code;
    private String msg;
    private Object data;

    ResponseVo(ResponseStatus status){
        this.code = status.getCode();
        this.msg = status.getMessage();
    }
    public static ResponseVo LOGIN_SUCCESS(){
        ResponseVo responseVo = new ResponseVo(ResponseStatus.SUCCESS);
        responseVo.setMsg("登录成功");
        return responseVo;
    }

    public static ResponseVo LOGIN_FAILURE(){
        ResponseVo responseVo = new ResponseVo(ResponseStatus.FAILURE);
        responseVo.setMsg("账号或密码错误");
        return responseVo;
    }

    public static ResponseVo SUCCESS(){
        return new ResponseVo(ResponseStatus.SUCCESS);
    }

    public static ResponseVo FAILURE(){
        return new ResponseVo(ResponseStatus.FAILURE);
    }

    public static ResponseVo AuthenticationFailure(){
        return new ResponseVo(ResponseStatus.AUTHENTICATION_FAILURE);
    }

    public ResponseVo setData(Object data) {
        this.data = data;
        return this;
    }



}
