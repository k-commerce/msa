package com.kcommerce.domain.order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {

    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClientFallback(cause);
    }

    @RequiredArgsConstructor
    static class ItemClientFallback implements ItemClient {

        private final Throwable cause;

        @Override
        public List<Item> getItemList(List<Long> itemIdList) {
            log.error(cause.getMessage());
            return Collections.emptyList();
        }
    }
}
