package com.biily.rpcfx.provider;

import com.billy.rpcfx.api.User;
import com.billy.rpcfx.api.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
