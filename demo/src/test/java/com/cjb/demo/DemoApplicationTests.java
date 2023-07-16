package com.cjb.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/*
* 使用jedis操作redis
* */
@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {

    @Test
    void testRedis() {
        Jedis jedis = new Jedis("localhost",6379);//获取连接

        jedis.set("username","cjb");//执行具体操作
        System.out.println(jedis.get("username"));
        jedis.close();//关闭连接
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void testDataRedisString(){
        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("username","easonChan");
        valueOperations.set("age","50");

        redisTemplate.opsForValue().set("city","HongKong",10l, TimeUnit.SECONDS);
    }

    @Test
    void testDataRedisHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("001","username","cjb");
        hashOperations.put("001","age","22");
        hashOperations.put("001","city","");

        final Set keys = hashOperations.keys("001");
        for (Object key :keys){
            System.out.println(key);
        }
    }

    @Test
    void testDataRedisList(){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("002","a","b","c");
        Long size = listOperations.size("002");
        for (int i = 0;i<size ;i++){
            String o = (String) listOperations.rightPop("002");
            System.out.println(o);
        }

        listOperations.leftPushAll("002","a","b","c");
        Long size1 = listOperations.size("002");
        for (int i = 0;i<size1 ;i++){
            String o = (String) listOperations.leftPop("002");
            System.out.println(o);
        }
    }

    @Test
    void testDataRedisSet(){
        final SetOperations setOperations = redisTemplate.opsForSet();

        setOperations.add("003","a","b","c","a");//存
        for (Object member : setOperations.members("003")) {  //取
            System.out.println(member);
        }

        setOperations.remove("003","a","b","c");//删除

    }

    @Test
    void testDataRedisZSet(){
        final ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("004","a",0.2);
        zSetOperations.add("004","b",0.3);
        zSetOperations.add("004","a",0.4);
        zSetOperations.add("004","d",0.5);

        Set<String> items = zSetOperations.range("004", 0, -1);
        for (String item : items){
            System.out.println(item);
        }
    }
}
