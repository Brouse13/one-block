package es.noobcraft.oneblock.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.server.ServerCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapServerCache implements ServerCache {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, List<String>> serverMap = Maps.newHashMap();

    @Override
    public Map<String, List<String>> getWorlds() {
        try {
            lock.readLock().lock();
            return ImmutableMap.copyOf(serverMap);
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addWorld(String server, String world) {
        try {
            lock.writeLock().lock();
            serverMap.computeIfPresent(server, (key, worlds) -> {
                worlds.add(world);
                return worlds;
            });
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeWorld(String server, String world) {
        try {
            lock.writeLock().lock();
            serverMap.computeIfPresent(server, (key, worlds) -> {
                worlds.remove(world);
                return worlds;
            });
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<String> getServer(String world) {
        try {
            lock.readLock().lock();
            for (String server : serverMap.keySet()) {
                if (serverMap.get(server).contains(world)) 
                    return Optional.of(server);
            }
        }finally {
            lock.readLock().unlock();
        }
        return Optional.empty();
    }

    @Override
    public void syncWorlds() {
        try {
            Logger.log(LoggerType.CONSOLE, "Syncing worlds from redis");
            lock.writeLock().lock();
            final Set<String> servers = serverMap.keySet();
            
            serverMap.clear();
            for (String server : servers)
                serverMap.putIfAbsent(server, OneBlockAPI.getServerLoader().getWorlds(server));
        }finally {
            Logger.log(LoggerType.CONSOLE, "Synced worlds from "+ serverMap.size()+ " servers");
            lock.writeLock().unlock();
        }
    }
}
