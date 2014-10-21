package web.page.crawl;

import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.glassdoor.com.au/Reviews/");
    }

    /**
     * This function is called when a page is fetched and ready to be processed by your program.
     */
    @Override
    public void visit(Page page) {

        try {
            String url = page.getWebURL().getURL();
            System.out.println("URL: " + url);

            if (page.getParseData() instanceof HtmlParseData) {

                Document companyReviewPage = Jsoup.connect(url).userAgent("Mozilla").get();
                Elements positions = companyReviewPage.select("span.authorJobTitle.cell.middle.padHorzSm");
                Elements pros = companyReviewPage.select("p.pros.noMargVert.notranslate");
                Elements cons = companyReviewPage.select("p.cons.noMargVert.notranslate");
                int max = positions.size();
                // max = max < pros.size() ? pros.size() : max;
                // max = max < cons.size() ? cons.size() : max;
                for (int i = 0; i < max; i++) {
                    System.out.print("Test");
                    System.out.print("|" + positions.get(i).text());
                    System.out.print("|" + pros.get(i).text());
                    System.out.println("|" + cons.get(i).text());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
