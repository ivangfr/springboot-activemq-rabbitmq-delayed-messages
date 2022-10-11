package com.ivanfranchin.delayedmessageproducerconsumer.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(@JsonSubTypes.Type(value = DelayedMessage.class, name = "DelayedMessage"))
public record DelayedMessage(String id, Instant expectedReturnTime) {
}
