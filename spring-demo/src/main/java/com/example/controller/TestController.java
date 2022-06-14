package com.example.controller;

import com.example.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // Lombok 테스트
    @GetMapping(value = "/test")
    public UserDTO test() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("seop");
        userDTO.setAge(28);
        
        return userDTO;
    }


}
