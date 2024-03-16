package org.mock.tinybank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {

    @GetMapping("/ping")
    String ping() {
        return "pong";
    }
}
