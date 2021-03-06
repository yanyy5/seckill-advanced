package com.seckill.controller;

import com.seckill.controller.viewObject.ItemVO;
import com.seckill.error.BusinessException;
import com.seckill.response.CommonReturnType;
import com.seckill.service.CacheService;
import com.seckill.service.ItemService;
import com.seckill.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    // controller: create item
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {

        // create item with service
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel returnItemModel = itemService.createItem(itemModel);

        // return information to frond-end: itemVO
        ItemVO itemVO = convertItemVOFromModel(returnItemModel);

        return CommonReturnType.create(itemVO);

    }

    private ItemVO convertItemVOFromModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if (itemModel.getPromoModel() != null){
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoPrice());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().
                    toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }
        else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }

    // controller: view item details
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){

        ItemModel itemModel = null;

        // get local cache
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_"+id);

        if (itemModel == null){
            // get info from redis by itemid
            itemModel = (ItemModel) redisTemplate.opsForValue().get("item_"+id);

            // if not in redis, visit the Service
            if (itemModel == null){
                itemModel = itemService.getItemById(id);
                redisTemplate.opsForValue().set("item_"+id, itemModel);
                redisTemplate.expire("item_"+id, 10, TimeUnit.MINUTES);
            }
            cacheService.setCommonCache("item_"+id, itemModel);
        }

        ItemVO itemVO = convertItemVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    // controller: view item list
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();

        // use stream API to convert item model list to itemVO list
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = convertItemVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }



}
