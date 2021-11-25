package com.dbms.placementapp.common;

public final class Constants {

    public static interface RESPONSE_STATUS {
        public final String SUCCESS = "SUCCESS";
        public final String FAILED = "ERROR";
    }

    public static interface PLACEMENT_STATUS {
        public final String REGISTERED = "registered";
        public final String NOT_REGISTERED = "not registered";
        public final String APPLIED = "applied";
        public final String REJECTED = "rejected";
        public final String PLACED = "placed";

    }

    public static interface APPLICATION_STATUS {
        public final String APPLIED = "applied";
        public final String REJECTED = "rejected";
        public final String PLACED = "placed";

    }

}
