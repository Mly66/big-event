package com.nbmly.service;

import com.nbmly.pojo.User;

public interface UserService {
    //根据用户名查询
    User findByUserName(String username);
    //注册
    void register(String username, String password);
    //更新
    void update(User user);
    //头像更新
    void updateAvatar(String avatarUrl);
    //更改密码
    void updatePwd(String newPwd);
}
