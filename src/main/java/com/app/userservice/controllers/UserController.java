package com.app.userservice.controllers;

import com.app.userservice.services.UserService;
import com.app.userservice.services.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    ResponseEntity<CompletableFuture<UserDto>> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.createUser(userDto));
    }

    @RequestMapping("/get/{id}")
    ResponseEntity<CompletableFuture<UserDto>> getUser(@PathVariable int id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @RequestMapping("/get")
    ResponseEntity<CompletableFuture<List<UserDto>>> getUser(@RequestBody List<Integer> ids) {
        return ResponseEntity.ok().body(userService.findUsersByIds(ids));
    }

    @RequestMapping("/get/{email}")
    ResponseEntity<CompletableFuture<UserDto>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.findByEmail(email));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CompletableFuture<UserDto>> updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CompletableFuture<Void>> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok().body(userService.deleteUserById(id));
    }
}
