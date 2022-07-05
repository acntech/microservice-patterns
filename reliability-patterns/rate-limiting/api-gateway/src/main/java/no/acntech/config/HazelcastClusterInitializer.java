package no.acntech.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class HazelcastClusterInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastClusterInitializer.class);
    private final Mono<HazelcastInstance> hazelcastCluster;

    public HazelcastClusterInitializer(String name, List<String> hosts) {
        ReplayProcessor<HazelcastInstance> processor = ReplayProcessor.create();
        Mono.just(hosts).publishOn(Schedulers.elastic())
                .map(members -> initCluster(name, members))
                .doOnNext(processor::onNext)
                .subscribe();
        this.hazelcastCluster = processor.next();
    }

    public Mono<HazelcastInstance> getHazelcastCluster() {
        return hazelcastCluster;
    }

    private HazelcastInstance initCluster(String name, List<String> members) {
        LOGGER.info("Starting new cluster for group {}", name);
        Config config = new Config();
        config.setClusterName(name);
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig().setEnabled(true);
        members.forEach(tcpIpConfig::addMember);
        return Hazelcast.newHazelcastInstance(config);
    }
}
