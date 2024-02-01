package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock{

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true);

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取当前线程的id
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success); // 如果success为null，返回false
    }

    @Override
    public void unlock() {
        //获取线程id
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        //获取锁中的id
        String lockId = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        //判断id是否一致
        if (threadId.equals(lockId)) {
            //一致则删除锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }

    }
}
