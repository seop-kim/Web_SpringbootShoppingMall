package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDTO {

    private Long cartItemId;

    private List<CartOrderDTO> cartOrderDTOList;
}
