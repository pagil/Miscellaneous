package web.page.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "http://www.glassdoor.com.au/Reviews/company-reviews.htm";
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();

        // String question = document.select(".row pros").text();
        // System.out.println("Question: " + question);

        // Elements links = document.getElementsByTag(HTML.Tag.P.toString());
        // for (Element link : links) {
        // String linkText = link.text();
        // System.out.println(linkText);
        // }

        Elements companies = document.select("a.item.org.emphasizedLink");
        for (Element company : companies) {
            String companyURL = company.absUrl("href");
            String companyName = company.text();
            Document companyPage = Jsoup.connect(companyURL).userAgent("Mozilla").get();
            Elements reviews = companyPage.select("a.eiCell.cell.reviews");
            String companyReviewURL = reviews.get(0).absUrl("href");
            Document companyReviewPage = Jsoup.connect(companyReviewURL).userAgent("Mozilla").get();
            Elements positions = companyReviewPage.select("span.authorJobTitle.cell.middle.padHorzSm");
            Elements pros = companyReviewPage.select("p.pros.noMargVert.notranslate");
            Elements cons = companyReviewPage.select("p.cons.noMargVert.notranslate");
            int max = positions.size();
            // max = max < pros.size() ? pros.size() : max;
            // max = max < cons.size() ? cons.size() : max;
            for (int i = 0; i < max; i++) {
                System.out.print(companyName);
                System.out.print("|" + positions.get(i).text());
                System.out.print("|" + pros.get(i).text());
                System.out.println("|" + cons.get(i).text());
            }
        }

        System.out.println("Done!");
    }
}
