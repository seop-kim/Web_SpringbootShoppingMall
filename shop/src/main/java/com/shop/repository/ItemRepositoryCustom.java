package com.shop.repository;

import com.shop.dto.ItemSearchDTO;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

}
