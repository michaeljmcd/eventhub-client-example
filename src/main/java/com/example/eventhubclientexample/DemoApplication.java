package com.example.eventhubclientexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import com.microsoft.azure.eventprocessorhost.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    // Event Hub values:
    @Value("${azure.eventHub.consumerGroup}")
    private String consumerGroup;

    @Value("${azure.eventHub.namespaceName}")
    private String namespace;

    @Value("${azure.eventHub.eventHubName}")
    private String eventHub;

    @Value("${azure.eventHub.policyName}")
    private String policyName;

    @Value("${azure.eventHub.policyKey}")
    private String policyKey;

    // Storage values (for checkpointing)
    @Value("${azure.storage.accountName}")
    private String storageAccount;

    @Value("${azure.storage.accountKey}")
    private String storageKey;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        final String storageConnectionString = createStorageConnectionString();
        final ConnectionStringBuilder connectionString = new ConnectionStringBuilder(namespace, eventHub, policyName, policyKey);

        log.info("Connecting to Event Hub {}", connectionString.toString());

        final EventProcessorHost host = new EventProcessorHost(eventHub, consumerGroup, connectionString.toString(), storageConnectionString);
        final EventProcessorOptions opts = new EventProcessorOptions();

        try {
            host.registerEventProcessor(LoggingProcessor.class, opts).get();
        } catch (Exception e) {
            log.error("Erorr adding event processor.", e);
        }

        // TODO: add cleanup.
    }

    private String createStorageConnectionString() {
        return String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;",
                storageAccount, storageKey);
    }
}
