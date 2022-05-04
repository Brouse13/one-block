package es.noobcraft.oneblock.server;

import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.server.ServerLoader;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisServerLoader implements ServerLoader {
    @Override
    public List<String> getWorlds(String server) {
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            return jedis.lrange("one-block:"+ server, 0, jedis.llen("one-block:"+ server));
        }
    }

    @Override
    public void addWorld(String server, String world) {
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            jedis.lpush("one-block:"+ server, world);
            jedis.publish("one-block", "1/"+ server+ "/"+ world);
        }
    }

    @Override
    public void removeWorld(String server, String world) {
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            jedis.lrem("one-block:"+ server, jedis.llen("one-block:"+ server), world);
            jedis.publish("one-block", "2/"+ server+ "/"+ world);
        }
    }
}
