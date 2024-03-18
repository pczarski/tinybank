package org.mock.tinybank.api;

import org.mock.tinybank.domain.UserService;
import org.mock.tinybank.dto.UserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @DeleteMapping("/{username}")
    public UserDto deactivateUser(@PathVariable String username) {
        return userService.deactivateUser(username);
    }
}