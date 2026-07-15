package com.bizpilot.business.controller;

import com.bizpilot.business.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebsiteController {

    private final WebsiteService websiteService;

    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @GetMapping("/{slug}")
    public String website(

            @PathVariable String slug,

            Model model) {

        return websiteService.render(slug, model);
    }

}