import javax.swing.*;

public class Product {
    private String prodName = "";
    private String url = "";
    private String price = "";
    private String imgSrc = "";

    public Product() {}
    public Product(String name, String url, String price, String imgSrc) {
        this.prodName = name;
        this.url = url;
        this.price = price;
        this.imgSrc = imgSrc;
    }

    public String getProdName() {
        return prodName;
    }

    public String getUrl() {
        return url;
    }

    public String getPrice() {
        return price;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
