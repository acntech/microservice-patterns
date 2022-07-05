package no.acntech.factory;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.RecoveryStrategy;
import io.github.bucket4j.grid.hazelcast.Hazelcast;
import no.acntech.config.HazelcastClusterInitializer;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ThrottlingRequestCounterFactory implements RequestCounterFactory {

    private final HazelcastClusterInitializer hazelcastClusterInitializer;

    public ThrottlingRequestCounterFactory(final HazelcastClusterInitializer hazelcastClusterInitializer) {
        this.hazelcastClusterInitializer = hazelcastClusterInitializer;
    }

    @Override
    public Mono<RequestCounter> create(String routeId, String apiKey, int limit, Duration duration) {
        Bandwidth bandwidth = Bandwidth.simple(limit, duration);
        return Mono.defer(() -> this.createBucket(routeId, apiKey, bandwidth))
                .map(ThrottlingRequestCounter::new);
    }

    private Mono<Bucket> createBucket(String routeId, String apiKey, Bandwidth bandwidth) {
        return this.hazelcastClusterInitializer
                .getHazelcastCluster()
                .map(hazelcastInstance -> hazelcastInstance.<String, GridBucketState>getMap(routeId))
                .map(map -> BucketConfiguration.builder()
                        .addLimit(bandwidth)
                        .build(map, apiKey, RecoveryStrategy.RECONSTRUCT));
    }
}
