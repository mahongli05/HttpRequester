package com.test.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.test.http.HttpRequester;
import com.test.http.HttpRequester.Request.Method;
import com.test.util.Utils;

public class HttpClientRequester extends HttpRequester {

    @Override
    public RequestResult execute(Request request) {

        BufferedReader reader = null;
        RequestResult result = new RequestResult();

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpRequestBase requestBase = null;

            Method method = request.getMethod();
            String url = request.getUrl();
            if (Method.GET == method) {
                requestBase = new HttpGet(url);
            } else if (Method.POST == method) {
                requestBase = new HttpPost(url);
            } else if (Method.PUT == method) {
                requestBase = new HttpPut(url);
            } else {
                throw new IllegalArgumentException(
                        String.format("Method %s is not support!", method.name()));
            }

            Map<String, String> forms = request.getForms();
            if (requestBase instanceof HttpEntityEnclosingRequestBase
                    && !forms.isEmpty()) {
                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                Set<Entry<String, String>> entries = forms.entrySet();
                for (Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    urlParameters.add(new BasicNameValuePair(key, value));
                }
                ((HttpEntityEnclosingRequestBase)requestBase).setEntity(
                        new UrlEncodedFormEntity(urlParameters));
            }

            // add headers
            Map<String, String> headers = request.getHeaders();
            addHeaders(requestBase, headers);

            HttpResponse response = client.execute(requestBase);
            result.code = response.getStatusLine().getStatusCode();

            reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
              stringBuilder.append(line + "\n");
            }

            String originalData = stringBuilder.toString();
            DataConventer dataConventer = request.getDataConventer();
            if (dataConventer != null) {
                dataConventer.convertData(originalData, result);
            } else {
                result.data = originalData;
            }
        } catch (Exception e) {
            result.exception = e;
        } finally {
            Utils.close(reader);
        }

        return result;
    }

    private static void addHeaders(HttpRequestBase requestBase, Map<String, String> headers) {

        if (!headers.isEmpty() && requestBase != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                requestBase.addHeader(key, value);
            }
        }
    }
}
