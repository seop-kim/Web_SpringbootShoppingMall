package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.CartItemDTO;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void 장바구니생성() {
        Item item = saveItem();
        Member member = saveMember();

        CartItemDTO cartItemDTO = new CartItemDTO();

        // 장바구니에 담는 로직 호출 결과 생성된 장바구니 상품 아이디를 cartItemId 변수에 저장한다.
        cartItemDTO.setItemId(item.getId());

        // 장바구니에 담을 수량을 정한다.
        cartItemDTO.setCount(5);

        Long cartItemId = cartService.addCart(cartItemDTO, member.getEmail());

        // 저장한 장바구니를 조회한다
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        // 저장을 위해 DTO에 저장한 수량과 저장된 수량이 같은지를 확인한다.
        assertEquals(cartItemDTO.getCount(), cartItem.getCount());
    }
}