package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.MenuItemRequest;
import com.bizpilot.business.dto.response.MenuItemResponse;
import com.bizpilot.business.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public MenuItemResponse addMenuItem( @Valid  @RequestBody MenuItemRequest request){

        return menuService.add(request);
    }

    @GetMapping
    public List<MenuItemResponse> getMenu() {

        return menuService.getMenu();

    }

    @PutMapping("/{sortOrder}")
    public MenuItemResponse update(
            @PathVariable Integer sortOrder,
            @RequestBody MenuItemRequest request) {

        return menuService.update(
                sortOrder,
                request);

    }

    @DeleteMapping("/{sortOrder}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer sortOrder) {

        menuService.delete(sortOrder);

        return ResponseEntity.noContent().build();
    }
}