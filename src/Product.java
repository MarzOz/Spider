import javax.swing.*;

public class Product {
//    private String keyword = "";
    private String prodName = "";
    private String url = "";
    private String price = "";
    private String imgSrc = "";

    public Product() {};
    public Product(String name, String url, String price, String imgSrc) {
//        this.keyword = key;
        this.prodName = name;
        this.url = url;
        this.price = price;
        this.imgSrc = imgSrc;
    }
}
