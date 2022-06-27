package com.shop.controller;

import com.shop.dto.ItemFormDTO;
import com.shop.dto.ItemSearchDTO;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.h2.engine.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemForm(Model model, @Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) { // 상품 등록 시 필수값이 비어있다면 다시 상품 등록 페이지로 전환한다.
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        try {
            itemService.saveItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "/item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDTO", new ItemFormDTO());
            return "item/itemForm";
        }

        return "item/itemForm";
    }


    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDTO, itemImgFileList);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDTO itemSearchDTO, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDTO, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDTO", itemSearchDTO);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDTO);
        return "item/itemDtl";
    }
}
