package my.vlong.java.webcrawler;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    private final int MAX_PAGE_TO_SEARCH = 300;

    private Set<String> visitedPage;
    private Queue<String> pageToVisited;

    public Crawler() {
        this.visitedPage = new HashSet<String>();
        this.pageToVisited = new LinkedList<String>();
    }


    public void search(String url) throws FileNotFoundException {
        CrawlerLeg crawlerLeg = new CrawlerLeg();
        List<CsvItem> items = new LinkedList<>();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        while (visitedPage.size() < MAX_PAGE_TO_SEARCH) {
            String currentUrl;
            if (pageToVisited.isEmpty()) {
                currentUrl = url;
            } else {
                currentUrl = nextUrl();
            }

            boolean isSuccess = crawlerLeg.crawl(currentUrl);
            if (isSuccess) {
                CsvItem item = crawlerLeg.getItem(currentUrl);
                if (!Objects.isNull(item)) {
                    items.add(item);
                }
            }
            this.pageToVisited.addAll(crawlerLeg.getLinks());
        }
        CsvWriter csvWriter = new CsvWriter();
        String file = "output/data.csv";
        csvWriter.writeCsvFile(file, items);

        System.out.println("**Done** visited " + this.visitedPage.size() + " web pages");
    }

    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = pageToVisited.poll();
        } while (visitedPage.contains(nextUrl));
        visitedPage.add(nextUrl);
        return nextUrl;
    }
}
