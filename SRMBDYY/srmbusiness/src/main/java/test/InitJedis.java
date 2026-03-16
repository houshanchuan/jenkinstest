package test;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

public class InitJedis {
    public Jedis getJedis(){
        Jedis jedis=null;
        String host = "175.24.134.85"; // 你的Redis服务器地址
        int port = 6379; // Redis端口，默认是6379

        // 创建Jedis对象，尝试连接到Redis服务器
        try {
            jedis = new Jedis(host, port);
            //System.out.println("连接到Redis... ");
            // 尝试ping Redis服务器，看是否响应成功
            //System.out.println(jedis);
            String response = jedis.ping();
            //System.out.println("服务器响应: " + response);
            if ("PONG".equals(response)) {
                //System.out.println("Redis连接成功！");
            } else {
                //System.out.println("Redis连接失败，服务器未响应PONG");
            }

        } catch (Exception e) {
            System.out.println("连接Redis时发生错误: " + e.getMessage());
        }
        finally {

        }
        return  jedis;
    }
}
