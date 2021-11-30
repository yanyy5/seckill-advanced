package com.seckill.service;

import com.seckill.error.BusinessException;
import com.seckill.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    // create item
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // view item list
    List<ItemModel> listItem();

    // view item details
    ItemModel getItemById(Integer id);

    // modify stock
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    // modify sales
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;

}
