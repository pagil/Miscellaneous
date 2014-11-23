package web.page.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import web.page.crawl.data.CompaniesOfInterest;

public class App {
    private static final String COMPANIES_LIST_PAGE = "http://www.glassdoor.com.au/Reviews/reviews-SRCH_IP";
    private static final int MAX_COMPANIES_PAGES = 348;
    private static int MAX_DEPTH = 200;

    // Sample command line:
    // viktor@viktorUVB:~/git/Miscellaneous/WebCrawl/target/classes$ java -Dsun.net.client.defaultReadTimeout=30000
    // -Dsun.net.client.defaultConnectTimeout=30000 -cp
    // .:/home/viktor/.m2/repository/org/jsoup/jsoup/1.7.3/jsoup-1.7.3.jar web.page.crawl.App >
    // ReviewsListOfCompanies.txt
    public static void main(String[] args) throws Exception {
        String url = "http://www.glassdoor.com.au/Reviews/company-reviews.htm";
        Document document = openUrl(url);

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

        for (int i = 1; i < MAX_COMPANIES_PAGES; i++) {
            url = COMPANIES_LIST_PAGE + i + ".htm";
            document = openUrl(url);
            if (document != null) {
                companies = document.select("a.item.org.emphasizedLink");
                for (Element company : companies) {
                    String companyURL = company.absUrl("href");
                    String companyName = company.text();
                    System.err.println("Company name = " + companyName);
                    if (like(companyName)) {
                        Document companyPage = openUrl(companyURL);
                        if (companyPage != null) {
                            Elements reviews = companyPage.select("a.eiCell.cell.reviews");
                            String companyReviewURL = reviews.get(0).absUrl("href");

                            Document companyReviewPage = openUrl(companyReviewURL);

                            crawlReviewPage(companyName, companyReviewPage, 0, url, companyReviewURL);
                        }
                    }
                }
            }
        }

        System.err.println("Done!");
    }

    public static void crawlReviewPage(String companyName, Document companyReviewPage, int depth,
            String companies_page_url, String company_review_page_url) throws Exception {
        if (companyReviewPage != null && depth < MAX_DEPTH) {
            Elements positions = companyReviewPage.select("span.authorJobTitle.cell.middle.padHorzSm");
            Elements date = companyReviewPage.select("tt.SL_date.margBot5");
            Elements pros = companyReviewPage.select("p.pros.noMargVert.notranslate");
            Elements cons = companyReviewPage.select("p.cons.noMargVert.notranslate");
            int max = positions.size();
            // max = max < pros.size() ? pros.size() : max;
            // max = max < cons.size() ? cons.size() : max;
            for (int i = 0; i < max; i++) {
                // System.out.print(companies_page_url);
                // System.out.print("|" + company_review_page_url);
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
                    Document nextCompanyReviewPage = openUrl(link.get(0).absUrl("href"));
                    crawlReviewPage(companyName, nextCompanyReviewPage, ++depth, companies_page_url, link.get(0)
                            .absUrl("href"));
                }
            }
        }
    }

    public static Document openUrl(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (Exception e) {
            System.err.println("Cannot open URL: " + url);
            e.printStackTrace(System.err);
        }
        return document;
    }

    public static boolean like(String companyName) {
        for (String cn : CompaniesOfInterest.COMPANIES) {
            if (companyName.toLowerCase().contains(cn.toLowerCase())) {
                System.err.println("Found company: " + companyName + " ~ like ~ " + cn);
                return true;
            }
        }
        return false;
    }

}
