package com.test.api;

import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.test.data.Result.GoodsPageResult;
import com.test.data.Result.GoodsResult;
import com.test.http.HttpConnectionRequester;
import com.test.http.HttpRequester;
import com.test.http.HttpRequester.Request;
import com.test.http.HttpRequester.Request.Method;
import com.test.http.HttpRequester.RequestResult;
import com.test.http.JsonDataConventer;

public class ApiUtil {

    public static final String BASE_API = "http://api.vdian.com/api";

    private static class MethodParams {
        String method;
        String access_token;
        String version = "1.0";
        String format = "json";

        public MethodParams(String method, String token) {
            this.method = method;
            this.access_token = token;
        }
    }

    /*http://api.vdian.com/api?param=
        {"page_num":1,"page_size":10,"orderby":1}
        &public={"method":"vdian.item.list.get",
        "access_token":"712d316173e43cdd0e07d9e10ad190a7",
        "version":"1.0","format":"json"}
     * */
    public static RequestResult getGoodsList(String token, int page, int size) throws URISyntaxException {

        JsonObject params = new JsonObject();
        params.addProperty("page_num", page);
        params.addProperty("page_size", size);

        Gson gson = new Gson();
        MethodParams methodParams = new MethodParams("vdian.item.list.get", token);
        String method = gson.toJson(methodParams, MethodParams.class);

        URIBuilder builder = new URIBuilder(BASE_API);
        String url = builder.addParameter("param", params.toString())
               .addParameter("public", method)
               .toString();

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrl(url);
        request.setDataConventer(new JsonDataConventer<GoodsPageResult>(GoodsPageResult.class));

        HttpRequester requester = generateHttpRequester();

        return requester.execute(request);
    }

    /*
     * http://api.vdian.com/api?public={"method":"vdian.item.add",
        "access_token":"1965eb8366956a025436fe73bc8aaf6a",
        "version":"1.0","format":"json"}&param={
        "imgs":["http://wd.geilicdn.com/vshop395640-1390204649-1.jpg"],
        "stock":"110","price":"350.00","item_name":"海北","skus":[],"merchant_code":"90"}
     * */
    public static RequestResult upload(String token, String params) throws URISyntaxException {

        Gson gson = new Gson();
        MethodParams methodParams = new MethodParams("vdian.item.add", token);
        String method = gson.toJson(methodParams, MethodParams.class);

        URIBuilder builder = new URIBuilder(BASE_API);
        String url = builder.addParameter("param", params.toString())
               .addParameter("public", method)
               .toString();

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrl(url);
        request.setDataConventer(new JsonDataConventer<GoodsResult>(GoodsResult.class));

        HttpRequester requester = generateHttpRequester();

        return requester.execute(request);
    }

    private static HttpRequester generateHttpRequester() {
        return new HttpConnectionRequester();
    }
}
