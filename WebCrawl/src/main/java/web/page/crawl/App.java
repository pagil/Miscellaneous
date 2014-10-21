package web.page.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    private static final int MAX_COMPANIES_PAGES = 348;
    private static int MAX_DEPTH = 200;

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
        // for (Element company : companies) {
        // String companyURL = company.absUrl("href");
        // String companyName = company.text();
        // Document companyPage = Jsoup.connect(companyURL).userAgent("Mozilla").get();
        // Elements reviews = companyPage.select("a.eiCell.cell.reviews");
        // String companyReviewURL = reviews.get(0).absUrl("href");
        //
        // Document companyReviewPage = Jsoup.connect(companyReviewURL).userAgent("Mozilla").get();
        //
        // crawlReviewPage(companyName, companyReviewPage, 0);
        //
        // }

        url = "http://www.glassdoor.com.au/Reviews/reviews-SRCH_IP";
        for (int i = 1; i < MAX_COMPANIES_PAGES; i++) {
            document = Jsoup.connect(url + i + ".htm").userAgent("Mozilla").get();
            companies = document.select("a.item.org.emphasizedLink");
            for (Element company : companies) {
                String companyURL = company.absUrl("href");
                String companyName = company.text();
                if (companyName.toLowerCase().contains("bank")) {
                    Document companyPage = Jsoup.connect(companyURL).userAgent("Mozilla").get();
                    Elements reviews = companyPage.select("a.eiCell.cell.reviews");
                    String companyReviewURL = reviews.get(0).absUrl("href");

                    Document companyReviewPage = Jsoup.connect(companyReviewURL).userAgent("Mozilla").get();

                    crawlReviewPage(companyName, companyReviewPage, 0);
                }
            }
        }

        System.out.println("Done!");
    }

    private static void crawlReviewPage(String companyName, Document companyReviewPage, int depth) throws Exception {
        if (depth < MAX_DEPTH) {
            Elements positions = companyReviewPage.select("span.authorJobTitle.cell.middle.padHorzSm");
            Elements date = companyReviewPage.select("tt.SL_date.margBot5");
            Elements pros = companyReviewPage.select("p.pros.noMargVert.notranslate");
            Elements cons = companyReviewPage.select("p.cons.noMargVert.notranslate");
            int max = positions.size();
            // max = max < pros.size() ? pros.size() : max;
            // max = max < cons.size() ? cons.size() : max;
            for (int i = 0; i < max; i++) {
                System.out.print(companyName);
                System.out.print("|" + date.get(i).text());
                System.out.print("|" + positions.get(i).text());
                System.out.print("|" + pros.get(i).text());
                System.out.println("|" + cons.get(i).text());
            }
            Elements next = companyReviewPage.select("li.next");
            if (!next.isEmpty()) {
                Elements link = next.select("a");
                if (!link.isEmpty()) {
                    Document nextCompanyReviewPage = Jsoup.connect(link.get(0).absUrl("href")).userAgent("Mozilla")
                            .get();
                    crawlReviewPage(companyName, nextCompanyReviewPage, ++depth);
                }
            }
        }
    }
}
