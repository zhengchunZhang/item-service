package com.heima.item.canal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.benmanes.caffeine.cache.Cache;
import com.heima.item.config.RedisHandler;
import com.heima.item.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
@CanalTable("tb_item")
public class itemHandler implements EntryHandler<Item> {
    @Autowired
    private RedisHandler redisHandler;
    @Autowired
    private Cache<Long, Item>  itemCache;
    @Override
    public void insert(Item item) {
        try {
            itemCache.put(item.getId() , item);
            redisHandler.saveItem(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Item before, Item after) {
        try {
            itemCache.put(after.getId() , after);
            redisHandler.saveItem(after);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Item item) {
        itemCache.invalidate(item);
        redisHandler.deleteItemById(item.getId());
    }
}
