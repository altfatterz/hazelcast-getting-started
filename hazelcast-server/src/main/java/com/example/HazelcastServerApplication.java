package com.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
public class HazelcastServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Config cfg = new Config();
        // By specifying group-name you can separate your clusters in a simple way; dev group, production group, test group, app-a group etc...
        cfg.getGroupConfig().setName("my-group");

        // you get a warning if you set it lower then 271 for 1 member:
        // The max size configuration for map "default" does not allow any data in the map. Given the current cluster size of 1 members with 271 partitions, max size should be at least 271.
        // Map size is forced set to 271 for backward compatibility
        cfg.getMapConfig("default").getMaxSizeConfig().setSize(300).setMaxSizePolicy(MaxSizeConfig.MaxSizePolicy.PER_NODE);
        cfg.getMapConfig("default").setEvictionPolicy(EvictionPolicy.LRU);
        //cfg.getMapConfig("default").setTimeToLiveSeconds(1);

        // a JVM can host multiple Hazelcast instances (nodes)
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        System.out.println("Instance name: " + instance.getName());

        Map<Integer, String> customers = instance.getMap("customers");

        IntStream.range(0,400).forEach(integer -> {
            customers.put(integer, "customer_" + integer);
        });

        Thread.sleep(1000);

        System.out.println("Map Size:" + customers.size());

        IntStream.range(0,400).forEach(i -> System.out.println("Customer " + i + ":" + customers.get(i)));

    }
}
