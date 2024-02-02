package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class ReddionTest {
    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    void setup() {
        lock = redissonClient.getLock("order");
    }

    @Test
    void method1() {
        //尝试获取锁
        boolean isLock = lock.tryLock();

        //若失败
        if (!isLock) {
            log.error("获取锁失败");
            //返回
            return;
        }
        try {
            //若成功
            log.info("获取锁成功");
            method2();
            log.info("开始执行业务1");
        } finally {
            log.warn("释放锁");
            lock.unlock();
        }

    }

    void method2() {
        //尝试获取锁
        boolean isLock = lock.tryLock();

        //若失败
        if (!isLock) {
            log.error("获取锁失败");
            //返回
            return;
        }
        try {
            //若成功
            log.info("获取锁成功");
            log.info("开始执行业务2");
        } finally {
            log.warn("释放锁");
            lock.unlock();
        }
    }

}
