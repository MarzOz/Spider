// jsoup selector-syntax
// https://jsoup.org/cookbook/extracting-data/selector-syntax
// GSON
//new Thread(() -> {
//
//                }).start();

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.*;

public class PageParsing {
    public static String keyword;

    public static void main(String[] args) throws IOException {
        String url = "https://www.google.com.tw";
        String amazon = "https://www.amazon.com/s?k=";
        String pchome = "https://ecshweb.pchome.com.tw/search/v3.3/?q=";
        String momo = "https://www.momoshop.com.tw/search/searchShop.jsp?keyword=";
        String yahoo = "https://tw.buy.yahoo.com/search/product?p=";


        System.out.print("Search> ");
        Scanner scanner = new Scanner(System.in);
        keyword = scanner.nextLine();

        String content = LAE(momo+keyword);
        System.out.println(content);
        if(content != null) {
            parsing(content, 2);
        }
    }

    private static String LAE(String url) throws IOException{
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(10000);
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = webClient.getPage(url);
        webClient.getCurrentWindow().setInnerHeight(3000);
        webClient.waitForBackgroundJavaScript(1000);
        String html = page.asXml();
        webClient.close();
        return html;
    }

    private static void parsing(String content, int select) throws IOException {
        int i;
//        int select = 1;
        Gson gson = new Gson();
        FileWriter fw1 = new FileWriter("out.txt");
        FileWriter fw2 = new FileWriter("select.txt");
        FileWriter fw3 = new FileWriter("productName.txt");
        FileWriter fw4 = new FileWriter("price.txt");
        FileWriter fw5 = new FileWriter("link.txt");
        FileWriter fw6 = new FileWriter("thumbnail.txt");
        FileWriter test = new FileWriter(keyword + ".json");
        FileWriter prodData = new FileWriter(keyword + ".data");
        Document document = Jsoup.parse(content);
        fw1.write(document.toString());
        fw1.close();
//        Elements items = document.select("#ItemContainer dl");
//        Elements items = document.select(".listArea li");
        if (select == 1) {
//            pchome
            Elements items = document.select("#ItemContainer dl");
            Element a;
            for (i = 0; i < items.size(); i++) {
                fw2.append("line " + i + ": " + items.get(i).toString() + "\n");
                Element item = items.get(i);
                String name = item.select(".prod_name > a").first().text();
                fw3.write(name + "\n");
                String price = item.select(".price").first().text();
                fw4.write(price + "\n");
                String link = item.select(".prod_name > a").first().attr("href");
                fw5.write(link + "\n");
                String thumbnail = item.select(".prod_img > img").first().attr("src");
                fw6.write(thumbnail + "\n");

                Product product = new Product(name, link, price, thumbnail);
//                test.write(gson.toJson(product) + ",\n");
                gson.toJson(product, test);
                prodData.write(name+"\n"+link+"\n"+price+"\n"+thumbnail+"\n");
            }
        }
        else if(select == 2) {
//            momo
            Elements items = document.select("div.listArea li");
            for (i = 0; i < items.size(); i++){
                fw2.append("line " + i + ": " + items.get(i).toString() + "\n");
                Element item = items.get(i);
                if ()
                String name = item.select(".prdName").first().text();
                fw3.write(name + "\n");
                String price = item.select("span.price").text();
                fw4.write(price + "\n");
                String url = item.select(".goodsUrl").first().attr("href");
                fw5.write("momoshop.com.tw"+url+"\n");
                String thumbnail = item.select("div.swiper-wrapper img").first().attr("src");
                fw6.write(thumbnail + "\n");
            }
        }

        fw2.close();
        fw3.close();
        fw4.close();
        fw5.close();
        fw6.close();
        test.close();
        prodData.close();
    }

}
