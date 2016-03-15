package com.test.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;

public abstract class HttpRequester {

    public static class RequestResult {
        public int code;
        public String message;
        public Exception exception;
        public Object data;
    }

    public interface DataConventer {
        void convertData(String data, RequestResult result);
    }

    public abstract RequestResult execute(Request request);

    public static class Request {

        private Method mMethod = Method.GET;
        private String mUrl;
        private Map<String, String> mHeaders = new HashMap<String, String>();
        private Map<String, String> mForms = new HashMap<String, String>();
        private Map<String, String> mParams = new HashMap<String, String>();
        private DataConventer mConventer;

        public void setMethod(Method method) {
            mMethod = method;
        }

        public Method getMethod() {
            return mMethod;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public String getUrl() throws UnsupportedEncodingException, URISyntaxException {

            return getURI().toString();
        }

        public URI getURI() throws UnsupportedEncodingException, URISyntaxException {

            URIBuilder builder = new URIBuilder(mUrl);
            if (!mParams.isEmpty()) {
                Set<Entry<String, String>> entries = mParams.entrySet();
                for (Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addParameter(key, value);
                }
            }

            return builder.build();
        }

        public void addHeader(String key, String value) {
            mHeaders.put(key, value);
        }

        public Map<String, String> getHeaders() {
            return mHeaders;
        }

        public void addForm(String key, String value) {
            mForms.put(key, value);
        }

        public Map<String, String> getForms() {
            return mForms;
        }

        public void addParam(String key, String value) {
            mParams.put(key, value);
        }

        public Map<String, String> getParams() {
            return mParams;
        }

        public void setDataConventer(DataConventer conventer) {
            mConventer = conventer;
        }

        public DataConventer getDataConventer() {
            return mConventer;
        }

        public enum Method {
            GET, POST, PUT
        }
    }
}
