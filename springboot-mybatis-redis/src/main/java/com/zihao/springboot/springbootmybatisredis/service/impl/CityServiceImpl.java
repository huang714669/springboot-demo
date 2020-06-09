package com.zihao.springboot.springbootmybatisredis.service.impl;

import com.zihao.springboot.springbootmybatisredis.dao.CityDao;
import com.zihao.springboot.springbootmybatisredis.domain.City;
import com.zihao.springboot.springbootmybatisredis.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取城市逻辑
     * 如果缓存存在，从缓存中获取城市信息
     * 如果缓存不存在，从DB中获取城市信息，然后插入缓存
     * @param id
     * @return
     */
    @Override
    public City findCityById(long id) {
        //从缓存中获取城市信息
        String key = "city" + id;
        ValueOperations<String, City> operations = redisTemplate.opsForValue();

        //缓存存在
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            City city = operations.get(key);

            LOGGER.info("CityServiceImpl.findCityById() : 从缓存中获取了城市 >> " + city.toString());
            return city;
        }

        LOGGER.info("CityServiceImpl.findCityById() : 缓存中无法获取 >> " + key);
        //从DB中获取城市信息
        City city = cityDao.findById(id);

        //插入缓存
        operations.set(key, city, 1000, TimeUnit.SECONDS);
        return city;
    }

    @Override
    public Long saveCity(City city) {
        return cityDao.saveCity(city);
    }

    /**
     * 更新城市逻辑
     * 如果缓存存在，删除
     * 如果缓存不存在，不操作
     * @param city
     * @return
     */
    @Override
    public Long updateCity(City city) {
        Long ret = cityDao.updateCity(city);

        //缓存存在，删除
        String key = "city_" + city.getId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            LOGGER.info("CityServiceImpl.updateCity() : 从缓存中删除城市 >> " + city.toString());
        }

        return ret;
    }

    @Override
    public Long deleteCity(Long id) {
        Long ret = cityDao.deleteCity(id);

        // 缓存存在，删除缓存
        String key = "city_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);

            LOGGER.info("CityServiceImpl.deleteCity() : 从缓存中删除城市 ID >> " + id);
        }
        return ret;
    }
}
