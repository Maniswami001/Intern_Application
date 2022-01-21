package com.example.internapplication;

public class productsmodel {
    private String shopname;
    private String shoptype;
    private String shop_location;
    private String shop_opentime;
    private String shop_closetime;

    private productsmodel(String shopname, String shoptype, String shop_location, String shop_opentime, String shop_closetime ){
        this.shopname=shopname;
        this.shoptype=shoptype;
        this.shop_location=shop_location;
        this.shop_opentime=shop_opentime;
        this.shop_closetime=shop_closetime;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getShop_opentime() {
        return shop_opentime ;
    }

    public void setShop_opentime(String shop_opentime) {
        this.shop_opentime = shop_opentime;
    }
    public String getShop_closetime() {
        return shop_closetime ;
    }

    public void setShop_closetime(String shop_closetime) {
        this.shop_closetime = shop_closetime;
    }

}
