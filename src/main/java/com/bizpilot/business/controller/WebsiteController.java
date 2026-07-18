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

    // Regex guard: slug mein dot (.) allowed nahi — isse favicon.ico,
    // manifest.json jaisi static file requests is controller se clash nahi karengi
    @GetMapping("/{slug:^(?!.*\\.).*$}")
    public String website(@PathVariable String slug, Model model) {
        System.out.println("slug : "+slug);
        return websiteService.render(slug, model);
    }

//    @GetMapping("/preview/{slug}/{theme}")
//    public String preview(
//            @PathVariable String slug,
//            @PathVariable String theme,
//            Model model) {
//
//        return websiteService.renderWithTheme(slug, theme, model);
//    }

//    @GetMapping("/w/{slug}")
//    public String website(
//
//            @PathVariable String slug,
//
//            Model model) {
//        System.out.println("slog : "+slug);
//
//        return websiteService.render(slug, model);
//    }


}