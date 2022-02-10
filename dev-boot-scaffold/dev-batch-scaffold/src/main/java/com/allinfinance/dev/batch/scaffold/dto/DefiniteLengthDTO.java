package com.allinfinance.dev.batch.scaffold.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author qipeng
 * @date 2022/2/9 15:41
 */
@Scope("prototype")
@Component
public class DefiniteLengthDTO {
    private String ISIN;
    private Integer number;
    private BigDecimal price;
    private String customer;

    public String getISIN() {
        return ISIN;
    }

    public void setISIN(String ISIN) {
        this.ISIN = ISIN;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "DefiniteLengthDTO{" +
                "ISIN='" + ISIN + '\'' +
                ", number=" + number +
                ", price=" + price +
                ", customer='" + customer + '\'' +
                '}';
    }
}
