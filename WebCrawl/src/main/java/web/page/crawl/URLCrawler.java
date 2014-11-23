package web.page.crawl;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;

public class URLCrawler {

    public static void main(String[] args) throws Exception {
        urls.parallelStream().forEach(URLCrawler::crawlUrl);
        System.err.println("Done!");
    }

    private static void crawlUrl(String url) {
        try {
            String[] chunks = url.split("/");
            chunks = chunks[chunks.length - 1].split("-");
            Document companyReviewPage = App.openUrl(url);
            App.crawlReviewPage(chunks[0], companyReviewPage, 0, null, null);
        } catch (Exception e) {
            System.err.println("Error processing URL: " + url);
            e.printStackTrace(System.err);
        }
    }

    private static Set<String> urls = new HashSet<String>();
    static {
        urls.add("http://www.glassdoor.com.au/Reviews/Amazon-com-Reviews-E6036.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Betfair-Reviews-E100546.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Aquent-Reviews-E3276.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/ASDA-Reviews-E10108.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Bluefin-Solutions-Reviews-E401263.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Envitia-Reviews-E532548.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/EPAM-Reviews-E15544.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Essence-Reviews-E449791.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Gebr-Heinemann-Reviews-E620914.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Hays-plc-Reviews-E10166.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Illumination-Works-Reviews-E436914.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Jurlique-Reviews-E116984.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Orbium-Reviews-E383567.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Reed-Technology-and-Information-Services-Reviews-E540068.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Sapient-Reviews-E5487.htm");
        urls.add("http://www.glassdoor.com.au/Reviews/Stagecoach-Reviews-E8702.htm");
    };
}
