package com.heima.item.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.IItemService;
import com.heima.item.service.IItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisHandler implements InitializingBean {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private IItemService itemService;
    @Autowired
    private IItemStockService iItemStockService;
    private  static  final ObjectMapper MAPPER = new ObjectMapper();
    //bean创建完，autowired之后执行
    @Override
    public void afterPropertiesSet() throws Exception {

        List<Item> itemList = itemService.list();
        for(Item item : itemList) {
            redisTemplate.opsForValue().set("item:id:"+item.getId(),MAPPER.writeValueAsString(item));
        }

        List<ItemStock> stockList = iItemStockService.list();
        for(ItemStock item : stockList) {
            redisTemplate.opsForValue().set("item:stock:id:"+item.getId(),MAPPER.writeValueAsString(item));
        }

    }

    public void saveItem(Item item) throws JsonProcessingException {
        redisTemplate.opsForValue().set("item:id:"+item.getId(),MAPPER.writeValueAsString(item));
    }
    public void deleteItemById(Long id){
        redisTemplate.delete("item:id:"+id);
    }
}
