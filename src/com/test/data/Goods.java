package com.test.data;

public class Goods {

    /*
     * {
            "itemid": "377074032",
            "item_name": "测试",
            "stock": 1,
            "price": "88.00",
            "sold": 0,
            "seller_id": "1005600",
            "istop": 1,
            "merchant_code": null,
            "fx_fee_rate": "0.00",
            "skus": [{
                "id": "1354421880",
                "title": "123",
                "price": "88.00",
                "stock": 1,
                "sku_merchant_code": null
            }],
            "imgs": ["http://wd.geilicdn.com/vshop1005600-1415182281695-2399763.jpg?w=480&h=0"],
            "thumb_imgs": ["http://wd.geilicdn.com/vshop1005600-1415182281695-2399763.jpg?w=110&h=110&cp=1"],
            "cates": [{
                "cate_id": "6349602",
                "cate_name": "水果",
                "sort_num": null
            },
            {
                "cate_id": "6350220",
                "cate_name": "电子产品",
                "sort_num": null
            }],
            "update_time": "2014-12-24 10:45:40"
        },
     * */

    public String itemid;
    public String item_name;
    public long stock;
    public String price;
    public long sold;
    public String seller_id;
    public int istop;
    public String merchant_code;
    public String fx_fee_rate;
    public String[] imgs;
    public String[] thumb_imgs;
    public String update_time;

    public Sku[] skus;
    public Cate[] cates;

    public static class Sku {

        public String id;
        public String title;
        public String price;
        public long stock;
        public String sku_merchant_code;

    }

    public static class Cate {

        public String cate_id;
        public String cate_name;
        public String sort_num;
    }

}
