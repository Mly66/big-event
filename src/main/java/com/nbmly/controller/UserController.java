package com.nbmly.controller;

import com.nbmly.pojo.Result;
import com.nbmly.pojo.User;
import com.nbmly.service.UserService;
import com.nbmly.utils.JwtUtil;
import com.nbmly.utils.Md5Util;
import com.nbmly.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")

//    public Result register(String username, String password){
//        if (username!=null && username.length()>=5 && username.length()<=16 &&
//            password!=null && password.length()>=5 && password.length()<=16)
//        {
//            //查询用户
//        User u = userService.findByUserName(username);
//        if (u==null){
//            userService.register(username,password);
//            return Result.success();
//            //没有占用，注册用户
//        }else {
//            //占用
//            return Result.error("用户名已被占用");
//        }
//        }else {
//            return Result.error("参数不合法");
//        }}
//
//    }
    //@Pattern()格式@Pattern(regexp = "^\\S{5,16}$")非空字符串长度在5-16
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
            //查询用户
        User u = userService.findByUserName(username);
        if (u==null){
            userService.register(username,password);
            return Result.success();
            //没有占用，注册用户
        }else {
            //占用
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$")String password){
        //查询用户
        User loginuser = userService.findByUserName(username);
        //判断是否存在
        if (loginuser==null){
            return  Result.error("用户不存在");
        }
        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(loginuser.getPassword())){
            //登录成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginuser.getId());
            claims.put("username",loginuser.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }
    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token){
        //根据用户名查询用户
//        Map<String, Object> map = JwtUtil.parseToken(token);
//        String username = (String) map.get("username");
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.update(user);
        return  Result.success();

    }
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        //1.手动校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (!StringUtils.hasLength(oldPwd) ||!StringUtils.hasLength(newPwd) ||!StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数");
        }

        //校验原密码是否正确
        //调用userservice根据用户名拿到原密码，和oldpwd比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }
        //校验两次密码输入是否一致
        if (!rePwd.equals(newPwd)){
            return Result.error("两次新密码输入不一致");
        }
        //2.调用service
        userService.updatePwd(newPwd);
        //删除Redis对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}