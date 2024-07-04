package com.sfz.rest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sfz.rest_api.model.User;
import com.sfz.rest_api.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sfz.rest_api.dto.ResponseForm;
import com.sfz.rest_api.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable String id) {
//        return userService.getUserById(id);
//    }

//    @GetMapping("/{nickname}")
//    public User getUserByNickname(@PathVariable("nickname") String nickname) {
//        return userService.getUserByNickname(nickname);
//    }

    @GetMapping("/{nickname}")
    public ResponseEntity<ResponseForm> getUserByNickname(@PathVariable("nickname") String nickname) {
        User user = userService.getUserByNickname(nickname);
        UserResponse userData = new UserResponse(user);
        ResponseForm response = new ResponseForm("ok", userData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createUser(@RequestPart("userData") User user,
                           @RequestPart(value = "profile_image", required = false) MultipartFile profileImage) {
        User newUser = userService.createUser(user);
        UserResponse userData = new UserResponse(newUser);
        ResponseForm response = new ResponseForm("ok", userData);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}