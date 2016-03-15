package com.test.data;

public abstract class Result<T> {

    /*
     * result": { },
       "status": {
            "status_code": 0,
            "status_reason": "success"
        }
     * */

    public T result;
    public Status status;

    public static class Status {
        int status_code;
        String status_reason;
    }

    public static class GoodsPageResult extends Result<GoodsPage> {

    }

    public static class GoodsResult extends Result<Goods> {

    }
}
