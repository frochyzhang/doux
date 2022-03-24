package com.allinfinance.dev.batch.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author qipeng
 * @date 2022/2/9 15:44
 */
@Scope("prototype")
@Component
public class DefiniteSeparatorDTO {
    private String company;
    private String year;
    private String channel;
    private Integer rank;
    private String name;
    private Integer count1;
    private Integer count2;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount1() {
        return count1;
    }

    public void setCount1(Integer count1) {
        this.count1 = count1;
    }

    public Integer getCount2() {
        return count2;
    }

    public void setCount2(Integer count2) {
        this.count2 = count2;
    }

    @Override
    public String toString() {
        return "DefiniteSeparatorDTO{" +
                "company='" + company + '\'' +
                ", year='" + year + '\'' +
                ", channel='" + channel + '\'' +
                ", rank=" + rank +
                ", name='" + name + '\'' +
                ", count1=" + count1 +
                ", count2=" + count2 +
                '}';
    }
}
