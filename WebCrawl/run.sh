cd ~/git/Miscellaneous/WebCrawl/target/classes/

java -Dsun.net.client.defaultReadTimeout=30000 -Dsun.net.client.defaultConnectTimeout=30000 -cp .:/home/viktor/.m2/repository/org/jsoup/jsoup/1.7.3/jsoup-1.7.3.jar web.page.crawl.App > ReviewsListOfCompanies.txt
