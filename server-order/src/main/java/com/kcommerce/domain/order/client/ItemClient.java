package com.kcommerce.domain.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "server-item", url = "http://localhost:8084")
public interface ItemClient {

    @GetMapping("/api/items")
    List<Item> getItemList(@RequestParam("itemIdList") List<Long> itemIdList);
}
