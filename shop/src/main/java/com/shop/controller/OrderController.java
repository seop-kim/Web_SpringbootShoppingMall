package com.shop.controller;

import com.shop.dto.OrderDTO;
import com.shop.dto.OrderHistDTO;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) { // orderDTO 객체에 데이터 바인딩 시 에러가 있는지 검사한다.
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            // 에러정보를 ResponseEntity 객체에 담아 반환한다.
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        // 로그인 유저 정보를 받기 위해 principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회한다.
        // 왜 이메일이 정보로 기입이 되지..?
        // SpringConfig 에서 userName 파라미터를 email 을 담아서 저장함.
        String email = principal.getName();
        Long orderId;

        try {
            // 화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출한다.
            orderId = orderService.order(orderDTO, email);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 결과 값으로 생성된 주문 번호와 요청이 성공했다는 HTTP 응답 상태 코드를 반환한다.
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        // 한번에 가지고 올 주문의 수는 4개로 설정한다.
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        // 현재 로그인한 회원은 이메일과 페이징 객체를 파라미터로 전달하여 화면에 전달할 주문목록 데이터를 리턴 값을 받는다.
        Page<OrderHistDTO> orderHistDTOList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody
    ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        // 자바스크립트에서 취소할 주문 번호는 조작이 가능하므로 다른 사람의 주문을 취소하지 못하도록 주문 취소 권한을 검사한다.
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 주문 취소 로직 호출
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
