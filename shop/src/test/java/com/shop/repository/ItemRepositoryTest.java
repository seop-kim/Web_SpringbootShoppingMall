package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;

import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext // 여옥성 컨텍스트를 사용하기 위해 해당 어노테이션을 사용하여 EntityManager 빈을 주입한다.
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트 1")
    public void Querydsl테스트1() {
        this.상품대량생성테스트();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); //  팩토리 객체를 생성하기 위해서는 EntityManager 객체를 넘겨야한다.
        QItem qItem = QItem.item; // Querydsl이 자동으로 생성해주는 객체

        // Query 를 작성하는 자바소스 코드
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch(); // fetch를 이용하여 쿼리 결과를 리스트로 반환한다.

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void Querydsl테스트2() {
        this.상품대량생성테스트2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 1003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price)); // 이 값보다 큰 것

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5); // 페이지 당 몇개의 콘텐츠를 출력할지
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable); // 조건을 넣고 조건에 맞는 값들을 가져온다.
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item resultItem : resultItemList) {
            System.out.println(resultItem.toString());
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void 상품생성테스트() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품 대량 저장 테스트")
    public void 상품대량생성테스트() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품 " + i);
            item.setPrice(1000 + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 대량 저장 테스트 2")
    public void 상품대량생성테스트2() {
        for (int i = 1; i <= 20; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품 " + i);
            item.setPrice(1000 + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);

            if (i <= 10) {
                item.setItemSellStatus(ItemSellStatus.SELL);
            } else {
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            }
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void 이름으로상품찾기테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품 0");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명 혹은 상세설명 조회 테스트")
    public void 이름혹은설명으로상품찾기테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품 1", "테스트 상품 상세 설명 4");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("일정 가격 이하의 상품 조회하기")
    public void 일정가격이하상품찾기테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByPriceLessThan(1003);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("일정가격 이하 상품을 조회하고 정렬")
    public void 일정가격이하상품찾고정렬테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(1006);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 상세설명 조회 테스트")
    public void JPQL설명으로상품찾기테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("NativeQuery를 이용한 상품 조회 테스트")
    public void NativeQuery테스트() {
        this.상품대량생성테스트();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }


}