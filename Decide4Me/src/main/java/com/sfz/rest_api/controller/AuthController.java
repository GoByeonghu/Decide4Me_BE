package com.sfz.rest_api.controller;

import com.sfz.rest_api.config.ApplicationProperties;
import com.sfz.rest_api.dto.CommentRequest;
import com.sfz.rest_api.dto.CommentResponse;
import com.sfz.rest_api.dto.ResponseForm;
import com.sfz.rest_api.dto.UserResponse;
import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.service.CommentService;
import com.sfz.rest_api.service.PostService;
import com.sfz.rest_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public AuthController(UserService userService,
                          ApplicationProperties applicationProperties) {
        this.userService = userService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/check-token")
    public ResponseEntity<ResponseForm> getUserByEmail() {
        String A =SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("LOG01: " + A);
        User userData = userService.getUserByEmail(A);
        UserResponse userResponse = new UserResponse(userData, applicationProperties.getServerAddress() + applicationProperties.getStaticURL());
        ResponseForm response = new ResponseForm("ok", userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
