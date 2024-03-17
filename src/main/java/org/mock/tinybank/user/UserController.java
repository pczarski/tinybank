package org.mock.tinybank.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping("/{userName}")
    public UserDto getUser(@PathVariable String userName) {
        return userService.getUser(userName);
    }
}