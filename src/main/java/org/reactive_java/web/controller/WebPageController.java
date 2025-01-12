package org.reactive_java.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebPageController {
    @GetMapping("/tasks")
    public String tasks() {
        return "tasks";
    }

    @GetMapping("/stats")
    public String getCommonStatistics() {
        return "stats";
    }

    @GetMapping("/stats/groups/{group}")
    public String getGroupStatistics(@PathVariable("group") String group, Model model) {
        model.addAttribute("group", group);
        return "stats";
    }

    @GetMapping("/stats/users/{user}")
    public String getUserStatistics(@PathVariable("user") String user, Model model) {
        model.addAttribute("user", user);
        return "stats";
    }

    @GetMapping("/data")
    public String getDataManipulation() {
        return "data_manipulation";
    }
}
