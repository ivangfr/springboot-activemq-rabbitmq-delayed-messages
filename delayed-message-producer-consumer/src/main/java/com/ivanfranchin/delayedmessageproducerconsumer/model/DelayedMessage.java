package com.ivanfranchin.delayedmessageproducerconsumer.model;

import java.io.Serializable;
import java.time.Instant;

public record DelayedMessage(String id, Instant expectedReturnTime) implements Serializable {
}
