package com.system.ui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UiController {
    @RequestMapping("/")
    String index() {
        return "index";
    }
}