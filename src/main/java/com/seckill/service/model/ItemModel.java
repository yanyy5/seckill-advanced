package com.seckill.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class ItemModel implements Serializable {

    private Integer id;

    @NotBlank(message = "The item name cannot be blank.")
    private String title;   // item name

    @NotNull(message = "The item price cannot be null.")
    @Min(value = 0, message = "The price must be larger than 0.")
    private BigDecimal price;

    @NotNull(message = "The item stock cannot be null.")
    private Integer stock;

    @NotBlank(message = "The item description cannot be blank.")
    private String description;

    private Integer sales;

    @NotBlank(message = "The item image cannot be blank.")
    private String imgUrl;

    private PromoModel promoModel;  // if promoModel != null, this item has promotion(status=1/2)

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public @NotNull(message = "The item price cannot be null.") @Min(value = 0, message = "The " +
            "price must be larger than 0.") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
