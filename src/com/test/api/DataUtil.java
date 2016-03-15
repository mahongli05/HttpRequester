package com.test.api;

import java.io.File;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import com.test.data.Goods;
import com.test.data.Goods.Cate;
import com.test.data.GoodsForUpload;
import com.test.data.GoodsPage;
import com.test.data.Result.GoodsPageResult;
import com.test.http.HttpRequester.RequestResult;
import com.test.util.FileUtil;

public class DataUtil {

    private static final int PAGE_SIZE = 20;

    public static void loadAndSaveAllGoods(String token) {

        GoodsPageResult result = null;
        int page = 1;
        do {
            result = loadGoods(token, page);
            saveToLocal(page, result);
            page++;
        } while (result != null && result.result != null
                && result.result.item_num !=  result.result.total_num);
    }

    public static void loadAndUploadAllGoods(String srcToken, String dstToken) {

        GoodsPageResult result = null;
        GoodsPage goodsPage = null;
        int page = 1;
        do {
            result = loadGoods(srcToken, page);
            goodsPage = result != null ? result.result : null;
            if (goodsPage != null && goodsPage.items != null) {
                for (Goods goods : goodsPage.items) {
                    uploadGoods(dstToken, goods);
                }
            }
            page++;
        } while (result != null && result.result != null
                && result.result.item_num !=  result.result.total_num);
    }

    public static void uploadGoods(String token, Goods goods) {
        GoodsForUpload goodsForUpload = new GoodsForUpload();
        goodsForUpload.item_name = goods.item_name;
        goodsForUpload.imgs = goods.imgs;
        goodsForUpload.merchant_code = goods.merchant_code;
        goodsForUpload.price = goods.price;
        goodsForUpload.skus = goods.skus;
        goodsForUpload.stock = goods.stock;
        Cate[] cates = goods.cates;
        if (cates != null) {
            String[] cate_ids = new String[cates.length];
            for (int i = 0; i < cates.length; i++) {
                cate_ids[i] = cates[i].cate_id;
            }
        }
        Gson gson = new Gson();
        try {
            ApiUtil.upload(token, gson.toJson(goodsForUpload));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static GoodsPageResult loadGoods(String token, int page) {

        try {
            RequestResult result = ApiUtil.getGoodsList(token, page, PAGE_SIZE);
            if (result != null && result.code == 200) {
                return (GoodsPageResult) result.data;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveToLocal(int page, GoodsPageResult result) {
        if (result != null) {
            Gson gson = new Gson();
            String content = gson.toJson(result);
            File file = new File("pages", String.valueOf(page));
            FileUtil.ensureFile(file);
            FileUtil.writeFile(file, content);
        }
    }
}
