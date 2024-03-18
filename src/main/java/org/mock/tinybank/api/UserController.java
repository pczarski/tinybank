package org.mock.tinybank.api;

import lombok.RequiredArgsConstructor;
import org.mock.tinybank.domain.UserRecord;
import org.mock.tinybank.domain.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserRecord createUser(@RequestBody UserRecord user) {
        return userService.createUser(user);
    }

    @GetMapping("/{username}")
    public UserRecord getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @DeleteMapping("/{username}")
    public UserRecord deactivateUser(@PathVariable String username) {
        return userService.deactivateUser(username);
    }
}