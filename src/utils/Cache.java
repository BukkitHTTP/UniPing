package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static volatile boolean isInCacheMaking = false;

    public static String getWithCache(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            return makeCache(key);
        }
        return entry.value;
    }

    private static String makeCache(String key) {
        if (isInCacheMaking) {
            return "{\"result\":\"throttled\",\"online\":false}";
        }
        isInCacheMaking = true;
        String result = Protocol.query(key);
        cache.put(key, new CacheEntry(result));
        isInCacheMaking = false;
        return result;
    }
}

class CacheEntry {
    final String value;
    final long time;

    CacheEntry(String value) {
        this.value = value;
        this.time = System.currentTimeMillis();
    }

    boolean isExpired() {
        return System.currentTimeMillis() - time > 1000L * 60 * 60;
    }
}
