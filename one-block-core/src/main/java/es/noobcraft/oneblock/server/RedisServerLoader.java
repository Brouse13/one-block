package es.noobcraft.oneblock.server;

import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.server.ServerLoader;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void teleportRequest(OneBlockProfile profile, String server) {
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            //Add the player name to the "one-block:teleport" key
            jedis.hset("one-block:teleport", profile.getOwner().getName()+ ":server", server);
            jedis.hset("one-block:teleport", profile.getOwner().getName()+ ":world", profile.getWorldName());
        }
    }

    @Override
    public Optional<String> hasTeleportRequest(String player, String server) {
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            //Check if the key "one-block:teleport" contains keys "playerName:server" and "playerName:world"
            if (jedis.hexists("one-block:teleport", player+ ":server") &&
                jedis.hexists("one-block:teleport", player+ ":world")) {
                if (jedis.hget("one-block:teleport", player+ ":server").equals(server))
                    return Optional.of(jedis.hget("one-block:teleport", player+ ":world"));
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeTeleportRequest(String player) {
        //Remove fro redis any teleport pending request
        try(Jedis jedis = Core.getRedisClient().getJedis()) {
            jedis.hdel("one-block:teleport", player+ ":server", player+ ":world");
        }
    }
}
