package com.example.eventhubclientexample;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingProcessor implements IEventProcessor {
    @Override
    public void onOpen(PartitionContext context) throws Exception {
        log.info("SAMPLE: Partition " + context.getPartitionId() + " is opening");
    }

    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception {
        log.info("SAMPLE: Partition " + context.getPartitionId() + " is closing for reason " + reason.toString());
    }

    @Override
    public void onError(PartitionContext context, Throwable error) {
        log.info("SAMPLE: Partition " + context.getPartitionId() + " onError: " + error.toString());
    }

    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> messages) throws Exception {
        log.info("I live");
    }
}
