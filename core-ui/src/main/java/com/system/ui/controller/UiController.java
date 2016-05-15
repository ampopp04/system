package com.system.ui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UiController {

    @RequestMapping("/")
    String index() {
        return "redirect:/js/index.html";
    }

    @RequestMapping("/systemMenuList")
    String getSystemMenuList() {
        String menuListJson = "{\n" +
                "  \"data\" :  [\n" +
                "{\n" +
                "\"id\" : \"1\",\n" +
                "\"text\" : \"menu1\",\n" +
                "\"iconCls\" : \"fa fa-group fa-lg\",\n" +
                "\"items\" : [\n" +
                "{\n" +
                "\"id\" : \"2\",\n" +
                "\"text\" : \"menu11\",\n" +
                "\"iconCls\" : \"xf0c0\",\n" +
                "\"className\" : \"panel\",\n" +
                "\"menu_id\" : \"1\",\n" +
                "\"leaf\" : true\n" +
                "},\n" +
                "{\n" +
                "\"id\" : \"3\",\n" +
                "\"text\" : \"menu12\",\n" +
                "\"iconCls\" : \"xf0007\",\n" +
                "\"className\" : \"panel\",\n" +
                "\"menu_id\" : \"1\",\n" +
                "\"leaf\" : true\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "]\n" +
                "}";
        return menuListJson;
    }
}