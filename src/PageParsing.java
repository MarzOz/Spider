// jsoup selector-syntax
// https://jsoup.org/cookbook/extracting-data/selector-syntax
// GSON

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import com.google.gson.*;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class PageParsing {
    private static final int PCHOME = 1;
    private static final int MOMO = 2;
    private static final int YAHOO = 3;
    private static final int AMAZON = 4;

    public static String keyword;

    public static JFrame frame = new JFrame("Comparator");
    static LinkedList<Product> best_prod = new LinkedList<>();
    static JPanel prodPnl[] = new JPanel[10];
    static JLabel prodList[][] = new JLabel[10][2];
    static JButton directBtn[] = new JButton[10];
    static JButton buyBtn[] = new JButton[10];

    static boolean loading = false;
    static int numResult;

    static String amazon = "https://www.amazon.com/s?k=";
    static String pchome = "https://ecshweb.pchome.com.tw/search/v3.3/?q=";
    static String momo = "https://www.momoshop.com.tw/search/searchShop.jsp?keyword=";
    static String yahoo = "https://tw.buy.yahoo.com/search/product?p=";

    public static void main(String[] args) throws IOException {
        String url = "https://www.google.com.tw";

        // create a GUI thread.
        javax.swing.SwingUtilities.invokeLater(PageParsing::createGUI);

/*
        System.out.print("Search> ");
        Scanner scanner = new Scanner(System.in);
        keyword = scanner.nextLine();

        String content = LAE(momo+keyword);
        System.out.println(content);
        if(content != null) {
            parsing(content, 2);
        }
*/
    }

    private static void createGUI() {
        // Main frame setup
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(0,0,800,800);

        // search panel
        JPanel keyPnl = new JPanel(new BorderLayout());
        JLabel keyLabel = new JLabel("Search:  ");
        keyLabel.setFont(new Font(keyLabel.getFont().toString(), Font.PLAIN, 16));
        JTextField searchField = new JTextField();
        keyPnl.setSize(frame.getWidth(), 50);
        // search button & setting icon
        JButton searchBtn = new JButton();
        ImageIcon img = new ImageIcon("search.png");
        searchBtn.setIcon(img);
        searchBtn.setContentAreaFilled(false);
        keyPnl.add(keyLabel, BorderLayout.WEST);
        keyPnl.add(searchField, BorderLayout.CENTER);
        keyPnl.add(searchBtn, BorderLayout.EAST);

        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n')
                    searchBtn.doClick();
            }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
        });

        // message panel
        JPanel msgPane = new JPanel();
        JLabel msgLabel = new JLabel("This is a testing message, don't mind me! ;)");
        msgPane.add(msgLabel);
        msgPane.setBounds(0,50,frame.getWidth(),30);
        keyPnl.add(msgPane, BorderLayout.SOUTH);

        // content panel
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setSize(new Dimension(frame.getWidth(), frame.getHeight()-80));
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.setBounds(0, 80, frame.getWidth(), 80);

        // initial list component
        for (int i = 0; i < 10; i++) {
            directBtn[i] = new JButton();
            prodList[i][0] = new JLabel();
            prodList[i][1] = new JLabel();
            prodPnl[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
        }
        searchBtn.addActionListener(e -> new Thread(() -> {
            loading = true;

//---------------------------------Still under construction----------------------------//
//            try {
                if ((keyword = searchField.getText()) == "") {
                    System.out.println("Invalid input");
                    msgLabel.setText("Oops! looks like you mistype something!");
                    throw new InputMismatchException();
                }else {
                    System.out.println("hi there");
                }
//            }catch (InputMismatchException imEx) {
//            }
//-------------------------------------------------------------------------------------//
            new Thread(() -> {
                while(loading) {
                    String loadingMsg = "Loading";
                    try {
                        msgLabel.setText(loadingMsg + ".");
                        sleep(1000);
                        msgLabel.setText(loadingMsg + "..");
                        sleep(1000);
                        msgLabel.setText(loadingMsg + "...");
                        sleep(1000);
                    } catch (InterruptedException ipEx) { }
                }
            }).start();


            String name, price, url, thumb;
            boolean searched = false;
            numResult = 0;
            best_prod.clear();
            // try to open keyword.data from storage.
            try {
                FileReader fr = new FileReader(keyword+".data");
                BufferedReader fin = new BufferedReader(fr);
                while ((name = fin.readLine()) != null) {
                    price = fin.readLine();
                    url = fin.readLine();
                    thumb = fin.readLine();
                    best_prod.add(new Product(name, price, url, thumb));
                }
                searched = true;
                fr.close();
            } catch (FileNotFoundException FNFe) {
                System.out.println("Previous result not found. Will Execute a new search.");
                msgLabel.setText("Seems you search \""+keyword+"\" for the first time, hmm!");
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            try {
                if (!searched) {
                    // parsing pchome
                    String content = LAE(pchome + keyword);
                    System.out.println(content);
                    if (content != null)
                        parsing(content, PCHOME);
                    // parsing momo
                    content = LAE(momo + keyword);
                    System.out.println(content);
                    if(content != null)
                        parsing(content, MOMO);
                }
            } catch (IOException ioEx) {
                loading = false;
//                sleep(2000);
                msgLabel.setText("Oops! Looks like somewhere goes wrong.");
                ioEx.printStackTrace();
            }
            Desktop desktop = Desktop.getDesktop();
            FileWriter results = null;
            try {
                results = new FileWriter(keyword+".data");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            while (numResult < 10) {
                Product p = best_prod.remove();
                try {
                    ImageIcon icon = new ImageIcon(new URL(p.getImgSrc()));
                    Image img1 = icon.getImage();
                    Image newimg = img1.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(newimg);
                    directBtn[numResult].setIcon(icon);
                    directBtn[numResult].setSize(50, 50);
                    results.write(p.getProdName() + "\n" +
                            p.getUrl() + "\n" +
                            p.getPrice() + "\n" +
                            p.getImgSrc() + "\n");
                }
                catch (MalformedURLException me) { me.printStackTrace();}
                catch (IOException ex) { ex.printStackTrace();}

                for (ActionListener al:
                     directBtn[numResult].getActionListeners()) {
                    directBtn[numResult].removeActionListener(al);
                }
                // add direct url to button
                directBtn[numResult].addActionListener(e1 -> {
                    try{
                    desktop.browse(new URL(p.getUrl()).toURI());
                    } catch (URISyntaxException uriEx) {
                        uriEx.printStackTrace();
                    } catch (IOException ioEx) {
                        ioEx.printStackTrace();
                    }
                });
                prodList[numResult][0].setText(p.getProdName());
                prodList[numResult][1].setText(p.getPrice());
//                        buyBtn[numResult] = new JButton("Buy");

                prodPnl[numResult].add(directBtn[numResult]);
                prodPnl[numResult].add(prodList[numResult][0]);
                prodPnl[numResult].add(prodList[numResult][1]);
                listPane.add(prodPnl[numResult]);

                numResult++;
            }
//            loading = false;
            try {
                sleep(2000);
                msgLabel.setText("find " + numResult + " results");
                results.close();
            } catch (InterruptedException ipEx) { }
            catch (IOException ex) { ex.printStackTrace();}
            finally {
                loading = false;
            }
        }).start());

        JScrollPane scrollPane = new JScrollPane(listPane);
        scrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()-100));

        contentPane.add(keyPnl, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        frame.add(contentPane);
        frame.pack();
        frame.setVisible(true);
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
        Gson gson = new Gson();
        FileWriter fw1 = new FileWriter("out.txt");
        FileWriter fw2 = new FileWriter("select.txt");
        FileWriter fw3 = new FileWriter("productName.txt");
        FileWriter fw4 = new FileWriter("price.txt");
        FileWriter fw5 = new FileWriter("link.txt");
        FileWriter fw6 = new FileWriter("thumbnail.txt");
//        FileWriter test = new FileWriter(keyword + ".json");
//        FileWriter prodData = new FileWriter(keyword + ".data");
        Document document = Jsoup.parse(content);
        fw1.write(document.toString());
        fw1.close();
        if (select == PCHOME) {
            Elements items = document.select("#ItemContainer dl");
            for (i = 0; i < items.size(); i++) {
                fw2.append("line " + i + ": " + items.get(i).toString() + "\n");
                Element item = items.get(i);
                String name = item.select(".prod_name > a").first().text();
                fw3.write(name + "\n");
                String price = item.select(".price").first().text();
                fw4.write(price + "\n");
                String link = "https:"+item.select(".prod_name > a").first().attr("href");
//                link = "https:" + link;
                fw5.write(link + "\n");
                String thumbnail = "https:"+item.select(".prod_img > img").first().attr("src");
//                thumbnail = "https:" + thumbnail;
                fw6.write(thumbnail + "\n");

//                Product product = new Product(name, link, price, thumbnail);
//                test.write(gson.toJson(product) + ",\n");
//                gson.toJson(product, test);
//                prodData.write(name+"\n"+link+"\n"+price+"\n"+thumbnail+"\n");
                // add the product into linkedlist
                best_prod.add(new Product(name,link, price, thumbnail));
            }
        }
        else if(select == MOMO) {
            Elements items = document.select("div.listArea li");
            for (i = 0; i < items.size(); i++){
                fw2.append("line " + i + ": " + items.get(i).toString() + "\n");
                Element item = items.get(i);
                if (!item.hasClass("edmBG")) {
                    String name = item.select(".prdName").first().text();
                    fw3.write(name + "\n");
                    String price = item.select("span.price").text();
                    fw4.write(price + "\n");
                    String url = "https://www.momoshop.com.tw" + item.select(".goodsUrl").first().attr("href");
                    fw5.write(url + "\n");
                    String thumbnail = item.select("div.swiper-wrapper img").first().attr("src");
                    fw6.write(thumbnail + "\n");

                    best_prod.add(new Product(name, url, price, thumbnail));
                }
                else
                    continue;
            }
        }
        best_prod.sort((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
        fw2.close();
        fw3.close();
        fw4.close();
        fw5.close();
        fw6.close();
//        test.close();
//        prodData.close();
    }
}
