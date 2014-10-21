package web.page.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GECrawl {

    public static void main(String[] args) throws Exception {
        String companyName = "GE";
        String companyReviewURL = "http://www.glassdoor.com.au/Reviews/GE-Reviews-E277.htm";

        Document companyReviewPage = Jsoup.connect(companyReviewURL).userAgent("Mozilla").get();
        Elements companies = companyReviewPage.select("a.item.org.emphasizedLink");

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

}
