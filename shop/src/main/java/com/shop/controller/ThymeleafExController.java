package com.shop.controller;

import com.shop.dto.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ValueConstants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")
    public String thymeleafExExample02(Model model) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemDetail("상품 상세 설명");
        itemDTO.setItemNm("테스트 상품 1");
        itemDTO.setPrice(10000);
        itemDTO.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDTO", itemDTO);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExExample03(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemNm("테스트 상품 " + i);
            itemDTO.setItemDetail("테스트 상품 상세 설명 " + i);
            itemDTO.setRegTime(LocalDateTime.now());
            itemDTO.setPrice(1000 * i);

            itemList.add(itemDTO);
        }

        model.addAttribute("itemList", itemList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExExample04(Model model) {

        List<ItemDTO> itemList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemNm("테스트 상품 " + i);
            itemDTO.setItemDetail("테스트 상품 상세 설명 " + i);
            itemDTO.setRegTime(LocalDateTime.now());
            itemDTO.setPrice(1000 * i);

            itemList.add(itemDTO);
        }

        model.addAttribute("itemList", itemList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExExample05() {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";

    }

    @GetMapping(value = "/ex07")
    public String thymeleafExExample07() {
        return "thymeleafEx/thymeleafEx07";
    }
}
