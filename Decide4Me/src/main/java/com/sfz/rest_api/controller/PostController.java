package com.sfz.rest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sfz.rest_api.model.User;
import com.sfz.rest_api.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
}
