package com.sfz.rest_api.service;

import com.sfz.rest_api.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);
    User getUserByNickname(String nickname);
    User createUser(User user);
    User updateUser(String id, User user);
    void deleteUser(String id);
}
