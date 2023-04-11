package com.kcommerce.domain.item.repository;

import com.kcommerce.domain.item.domain.Item;
import com.kcommerce.domain.item.dto.ItemDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kcommerce.domain.item.domain.QCategoryItem.categoryItem;
import static com.kcommerce.domain.item.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> searchItem(ItemDto.ItemSearchCondition itemSearchCondition) {
        return queryFactory
                .select(item)
                .from(itemSearchCondition.getCategoryId() != null ? categoryItem : item)
                .where(
                        gtCursorId(itemSearchCondition.getCursorId()),
                        eqCategoryId(itemSearchCondition.getCategoryId()),
                        inItemIdList(itemSearchCondition.getItemIdList()),
                        containsName(itemSearchCondition.getName())
                )
                .limit(10)
                .fetch();
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        return categoryId != null ? categoryItem.category.id.eq(categoryId) : null;
    }

    private BooleanExpression inItemIdList(List<Long> itemIdList) {
        return itemIdList != null ? item.id.in(itemIdList) : null;
    }

    private BooleanExpression containsName(String name) {
        return name != null ? item.name.contains(name) : null;
    }

    private BooleanExpression gtCursorId(Long cursorId) {
        return cursorId != null ? item.id.gt(cursorId) : null;
    }
}
