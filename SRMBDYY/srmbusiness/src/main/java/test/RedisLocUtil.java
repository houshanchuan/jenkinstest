package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;
public class RedisLocUtil {
    @Autowired
    private InitJedis initJedis=new InitJedis();
    private final int lockExpire = 10000; // 锁过期时间，单位毫秒

    /** 不成功返回null;或者异常
     * @param lockExpire
     * @param lockey
     * @return
     */
    public String lock(String lockey,int lockExpire){
        String lockValue= UUID.randomUUID().toString();
        Long endTime=System.currentTimeMillis()+lockExpire;
        String retIdentifier = null;
        Jedis jedis=null;
        lockey="lock:"+lockey;
        try{
            jedis= initJedis.getJedis();
            while (System.currentTimeMillis()<endTime){
                if(jedis.setnx(lockey,lockValue)==1){
                    jedis.expire(lockey,lockExpire);
                    retIdentifier=lockValue;
                    System.out.println("加锁成功");
                    return retIdentifier;
                }
                if(jedis.ttl(lockey)==-1){
                    jedis.expire(lockey,lockExpire);
                }
            }

        }catch (JedisException e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return retIdentifier;
    }
    /**
     * 释放锁
     * @param lockName   锁的key
     * @param identifier 释放锁的标识
     * @return
     */
    public boolean unLock(String lockName, String identifier) {
        Jedis jedis = null;
        String lockKey = "lock:" + lockName;
        boolean success = false;
        try {
            jedis = initJedis.getJedis();
            while (true) {
                // 监视lock，准备开始事务
                jedis.watch(lockKey);
                // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                if (identifier.equals(jedis.get(lockKey))) {
                    Transaction transaction = jedis.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    success = true;
                }
                jedis.unwatch();
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return success;
    }


}
