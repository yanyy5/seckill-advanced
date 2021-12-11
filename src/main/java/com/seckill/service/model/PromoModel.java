package com.seckill.service.model;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

public class PromoModel implements Serializable {

    private Integer id;

    private Integer status; // 1-没开始 2-进行中 3-已结束

    private String promoName;

    private DateTime startDate;

    private DateTime endDate;

    private Integer itemId;

    private BigDecimal promoPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
