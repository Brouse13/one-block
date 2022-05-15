package es.noobcraft.oneblock.api.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMap<K, V> extends ConcurrentHashMap<K, V> {
    private final ConcurrentHashMap<K, Long> expires = new ConcurrentHashMap<>();
    private final Long expireTime;
    private boolean active;

    public CacheMap(Long expireTime) {
        this.active = true;
        this.expireTime = expireTime;
        new CacheScheduler().start();
    }

    @Override
    public V put(K key, V value) {
        if (!active) throw new IllegalStateException("This cache is closed, please use another to perform actions");

        expires.put(key, (long) (System.currentTimeMillis() + expireTime + (Math.random() * 10)));
        return super.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        if (!active) throw new IllegalStateException("This cache is closed, please use another to perform actions");

        for (K key : map.keySet()) put(key, map.get(key));
    }

    public V putIfAbsent(K key, V value) {
        if (!active) throw new IllegalStateException("This cache is closed, please use another to perform actions");

        if (!contains(key))
            return put(key, value);
        else
            return get(key);
    }

    public void close() {
        active = false;
    }

    class CacheScheduler extends Thread {
        @Override
        public void run() {
            while (active) {
                try {
                    Thread.sleep(1000);
                    expiredCache();
                }catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }

        private void expiredCache() {
            long millis = System.currentTimeMillis();

            for (K key : expires.keySet()) {
                if (expires.get(key) <= millis)
                    expires.remove(key);
            }
        }
    }
}
