package com.zihao.springboot.springbootmybatisredis.service;

import com.zihao.springboot.springbootmybatisredis.domain.City;

public interface CityService {

    City findCityById(long id);

    Long saveCity(City city);

    Long updateCity(City city);

    Long deleteCity(Long id);
}
