package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HazelcastClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("192.168.0.159:5701");
        clientConfig.getGroupConfig().setName("my-group");

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        IMap map = hazelcastInstance.getMap("customers");
        System.out.println("Map Size:" + map.size());

    }
}
