package com.example.trackingnumbergenerator.util;

import java.time.Instant;

public class SnowflakeIdGenerator {
    private static final long EPOCH = 1609459200000L;
    private static final long NODE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    private final long nodeId;
    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId >= (1 << NODE_ID_BITS)) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + ((1 << NODE_ID_BITS) - 1));
        }
        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << (NODE_ID_BITS + SEQUENCE_BITS))
                | (nodeId << SEQUENCE_BITS)
                | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        long timestamp = timestamp();
        while (timestamp <= currentTimestamp) {
            timestamp = timestamp();
        }
        return timestamp;
    }

    private long timestamp() {
        return Instant.now().toEpochMilli();
    }
}
