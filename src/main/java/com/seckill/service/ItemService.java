package com.seckill.service;

import com.seckill.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    // create item
    ItemModel createItem(ItemModel itemModel);

    // view item list
    List<ItemModel> listItem();

    // view item details
    ItemModel getItemById(Integer id);

}
