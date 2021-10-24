package com.flight.enums;

public enum InboundOutbound {
    IN_BOUND(1),
    OUT_BOUND(2);


    InboundOutbound(long id) {
        this.id = id;
    }

    private long id;

    public long id() {
        return this.id;
    }
}
