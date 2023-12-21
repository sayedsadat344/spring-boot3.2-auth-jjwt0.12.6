package com.springbootauth.springbootauthunticationandauthurization.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("The demo is running");
    }
}
