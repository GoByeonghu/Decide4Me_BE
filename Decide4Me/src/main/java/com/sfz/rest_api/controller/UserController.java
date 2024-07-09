package com.sfz.rest_api.controller;

import com.sfz.rest_api.config.ApplicationProperties;
import com.sfz.rest_api.dto.PasswordRequest;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.service.FileStorageService;
import com.sfz.rest_api.service.PostService;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostService postService;
    private final ApplicationProperties applicationProperties;
    private final FileStorageService fileStorage;

    @Autowired
    public UserController(UserService userService,
                          PostService postService,
                          FileStorageService fileStorage,
                          ApplicationProperties applicationProperties) {
        this.userService = userService;
        this.postService = postService;
        this.fileStorage = fileStorage;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ResponseForm> createUser(@RequestPart("userData") User user,
                                                   @RequestPart(value = "profile_image", required = false) MultipartFile profileImage) {
        try {
            User newUser = userService.createUser(user);

            if (profileImage != null && !profileImage.isEmpty()) {
                String newProfilePath = fileStorage.storeUserProfile(profileImage, newUser.getId());
                newUser.setProfileImage(newProfilePath);
                newUser = userService.updateUser(newUser.getId(),newUser);
            }

            UserResponse userData = new UserResponse(newUser, applicationProperties.getServerAddress() + applicationProperties.getStaticURL());
            ResponseForm response = new ResponseForm("ok", userData);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<ResponseForm> getUserByNickname(@PathVariable("nickname") String nickname) {
        User user = userService.getUserByNickname(nickname);
        UserResponse userData = new UserResponse(user, applicationProperties.getServerAddress() + applicationProperties.getStaticURL());
        ResponseForm response = new ResponseForm("ok", userData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/{nickname}")
    public ResponseEntity<ResponseForm> updateUser(@PathVariable("nickname") String nickname,
                                                   @RequestPart(value ="userData", required = false) User user,
                                                   @RequestPart(value = "profile_image", required = false) MultipartFile profileImage) {
        try {
            User legacyUser = userService.getUserByNickname(nickname);

            if(user == null){
                user = legacyUser;
            }
            if (profileImage != null && !profileImage.isEmpty()) {
                //이미 이미지 있는 경우 해당 이미지 삭제
                if(legacyUser.getProfileImage() != null) {
                    fileStorage.deleteFile(applicationProperties.getFileServer()+legacyUser.getProfileImage());
                }
                String newProfilePath = fileStorage.storeUserProfile(profileImage, legacyUser.getId());
                user.setProfileImage(newProfilePath);
            }

            User newUser = userService.updateUser(legacyUser.getId(), user);

            UserResponse userData = new UserResponse(newUser, applicationProperties.getServerAddress() + applicationProperties.getStaticURL());
            ResponseForm response = new ResponseForm("ok", userData);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{nickname}")
    public ResponseEntity<ResponseForm> updateUserPassword(@PathVariable("nickname") String nickname,
                                                           @RequestParam("target") String target,
                                                           @RequestBody PasswordRequest passwordRequest) {
        if(target.equals("password")){
            User user = userService.getUserByNickname(nickname);
            user.setPassword(passwordRequest.getPassword());
            userService.updateUser(user.getId(), user);
            ResponseForm response = new ResponseForm("ok", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseForm response = new ResponseForm("error", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<ResponseForm> deleteUser(@PathVariable("nickname") String nickname, @RequestBody PasswordRequest passwordRequest) {
        try {
                User user = userService.getUserByNickname(nickname);

                // Validate the password (add your validation logic here)
                if ( ! user.getPassword().equals( passwordRequest.getPassword() ) ) {
                    ResponseForm response = new ResponseForm("error", "Invalid password");
                    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
                }

                // post delete
                List<Post> posts = postService.getPostsByUserId(user.getId());
                for(Post post : posts){
                    postService.deletePost(post.getId());
                }

                if(user.getProfileImage() != null) {
                    fileStorage.deleteFile(applicationProperties.getFileServer()+user.getProfileImage());
                }
                userService.deleteUser(user.getId());
                ResponseForm response = new ResponseForm("ok", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File delete failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/check-duplicated-filed")
    public ResponseEntity<ResponseForm> checkDuplicatedField(@RequestParam("target") String target,
                                                             @RequestParam("value") String value) {

        if ("nickname".equals(target)) {
            if(userService.existsByNickname(value)){
                return new ResponseEntity<>(new ResponseForm("already exist", null), HttpStatus.CONFLICT);
            }
        } else if ("email".equals(target)) {
            if(userService.existsByEmail(value)){
                return new ResponseEntity<>(new ResponseForm("already exist", "Invalid target field"), HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new ResponseForm("error", "Invalid target field"), HttpStatus.BAD_REQUEST);
        }

        ResponseForm response = new ResponseForm("ok, not duplicated", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}