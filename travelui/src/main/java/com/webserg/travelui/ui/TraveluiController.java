package com.webserg.travelui.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;

@Controller
public class TraveluiController {

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("user", "serg");
        return "index";
    }

}
