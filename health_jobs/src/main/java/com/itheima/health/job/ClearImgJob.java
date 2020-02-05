package com.itheima.health.job;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    JedisPool jedisPool;
    public void clearImg(){
        System.out.println("开始清理垃圾图片...");
        Set<String>set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 获取不同的pic文件名
        Iterator<String>iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("需要删除的图片"+pic);
            //删除七牛云上的垃圾图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除Redis中的2个集合不一样的数据，删除key值为setmealPicResource
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);


        }
    }
}
