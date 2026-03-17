package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody UserRequest request){

        User user = userService.createUser(request);

        return new ApiResponse<>(0, "success", user);
    }

    @GetMapping
    public ApiResponse<List<User>> getAllUser(){

        List<User> users = userService.getUsers();

        return new ApiResponse<>(0, "success", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id){

        User user = userService.getUserById(id);

        return new ApiResponse<>(0, "success", user);
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUserById(@PathVariable Long id,
                                            @RequestBody UserRequest request){

        User user = userService.updateUserById(id, request);

        return new ApiResponse<>(0, "success", user);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id){

        userService.deleteUserById(id);

        return new ApiResponse<>(0, "success", "User deleted");
    }
}
