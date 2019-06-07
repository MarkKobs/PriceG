package com.example.guessprice;

public class Goods {
    private Integer goodsID;//商品编号
    private int goodsPrice;//商品价格
    private String goodsName;//商品名
    private byte[] goodsImage;//商品图片地址

    public Integer getGoodsID(){return this.goodsID;}
    public int getGoodsPrice(){return this.goodsPrice;}
    public String getGoodsName(){return this.goodsName; }
    public byte[] getGoodsImage(){return this.goodsImage;}
}
