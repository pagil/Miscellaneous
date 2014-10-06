package web.page.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "http://www.glassdoor.com.au/Reviews/Telstra-Reviews-E6563.htm";
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();

        // String question = document.select(".row pros").text();
        // System.out.println("Question: " + question);

        // Elements links = document.getElementsByTag(HTML.Tag.P.toString());
        // for (Element link : links) {
        // String linkText = link.text();
        // System.out.println(linkText);
        // }

        Elements answerers = document.select(".pros");
        for (Element answerer : answerers) {
            System.out.println("PROS: " + answerer.text());
        }

        System.out.println("Done!");
    }
}
