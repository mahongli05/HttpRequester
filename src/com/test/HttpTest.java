package com.test;

import com.test.http.HttpClientRequester;
import com.test.http.HttpRequester;

public class HttpTest {

    /*
     *
     * */

    static HttpRequester createHttpRequester() {
        return new HttpClientRequester();
    }

    public static class Result<T> {
        public int status;
        public String msg;
        public T data;
    }

    public static class Discovery {
        public String icon;
        public String title;
        public String cmd;
        public String desc;
        public String tag;
        public int notify;
    }

    public static class DiscoverList {
        public Discovery[] list;
    }

    public static class DiscoveryResult extends Result<DiscoverList> {

    }
}
