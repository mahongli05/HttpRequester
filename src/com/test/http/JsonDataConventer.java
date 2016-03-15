package com.test.http;

import com.google.gson.Gson;
import com.test.http.HttpRequester.DataConventer;
import com.test.http.HttpRequester.RequestResult;

public class JsonDataConventer<T> implements DataConventer {

    private final Class<T> mClassType;

    public JsonDataConventer(Class<T> type) {
        mClassType = type;
    }

    @Override
    public void convertData(String data, RequestResult result) {
        try {
            Gson gson = new Gson();
            result.data = gson.fromJson(data, mClassType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}