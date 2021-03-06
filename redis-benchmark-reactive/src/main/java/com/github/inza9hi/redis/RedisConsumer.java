package com.github.inza9hi.redis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


public class RedisConsumer implements Runnable {

  private LinkedBlockingQueue<String> queue;
  private ReactiveStringRedisTemplate redisTemplate;
  private CountDownLatch downLatch;


  public RedisConsumer(LinkedBlockingQueue<String> queue,ReactiveStringRedisTemplate redisTemplate,CountDownLatch downLatch){
    this.queue = queue;
    this.redisTemplate = redisTemplate;
    this.downLatch = downLatch;
  }

  @Override
  public void run() {
    try {
      String key = queue.poll();
      while (key != null) {
        redisTemplate.opsForValue().set(key, key).block();
        key = queue.poll();
      }
    }finally {
      downLatch.countDown();
    }

  }
}
