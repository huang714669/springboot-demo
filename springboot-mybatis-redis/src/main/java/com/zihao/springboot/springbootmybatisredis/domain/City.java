package com.zihao.springboot.springbootmybatisredis.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 城市实体类
 */
@Data
public class City implements Serializable {
    private static final Long serialVersionUID = -1L;

    private Long id;

    private Long provinceId;

    private String cityName;

    private String description;

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", provinceId=" + provinceId +
                ", cityName='" + cityName + '\'' +
                ", descriptions='" + description + '\'' +
                '}';
    }
}
