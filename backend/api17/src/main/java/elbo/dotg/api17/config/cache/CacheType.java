package elbo.dotg.api17.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    CATEGORY_CACHE("categoryCache", 10, 10000);

    private final String cacheName;
    private final int expired;
    private final int limit;
}
