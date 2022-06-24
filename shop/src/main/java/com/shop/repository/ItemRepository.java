package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    List<Item> findByItemNm(String itemNm); // 이름으로 조회하기

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail); // 이름 혹은 상세설명으로 조회하기

    List<Item> findByPriceLessThan(Integer price); // 일정 가격 이하의 상품들 조회하기

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); // 일정 가격 이하의 상품들을 내림차순으로 정렬하여 조회

    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "SELECT * FROM item i WHERE i.item_detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
