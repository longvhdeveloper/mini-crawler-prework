package my.vlong.java.webcrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CrawlerLeg {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links;
    private Document document;

    public CrawlerLeg() {
        links = new LinkedList<String>();
    }

    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document document = connection.get();
            this.document = document;

            if (connection.response().statusCode() == 200) {
                System.out.println("\n**Visiting** Received web page at " + url);
            } else {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }

            Elements elements = document.select("a[href]");
            System.out.println("Found (" + elements.size() + ") links");

            elements.forEach(element -> {
                String link = element.absUrl("href");
                link = link.replace("#box_comment", "");
                if (!links.contains(link)) {
                    links.add(link);
                }
            });

        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Invalid URL : " + url);
            return false;
        }

        return true;
    }

    public CsvItem getItem(String url) {
        String title = null;
        String publishDate = null;
        String author = null;

        // Defensive coding. This method should only be used after a successful crawl.
        if (this.document == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return null;
        }

        Element titleItem = document.select("h1.title_news_detail").first();
        if (!Objects.isNull(titleItem)) {
            title = titleItem.text();
            //            System.out.println(title);
        }

        Element timeElement = document.select("span.time").first();
        if (!Objects.isNull(timeElement)) {
            publishDate = timeElement.text();
            //            System.out.println(publishDate);
        }

        Element authorElement = document.select("p.author_mail > strong").first();
        if (!Objects.isNull(authorElement)) {
            author = authorElement.text();
            //            System.out.println(author);
        } else {
            authorElement = document.select("p.Normal[style*=text-align:right;]").first();
            if (!Objects.isNull(authorElement)) {
                author = authorElement.text();
            } else {
                authorElement = document.select("p.author").first();
                if (!Objects.isNull(authorElement)) {
                    author = authorElement.text();
                } else {
                    authorElement = document.select("p[style*=text-align:right;]").first();
                    if (!Objects.isNull(authorElement)) {
                        author = authorElement.text();
                    } else {
                        authorElement = document.select("div.author > a").first();
                        if (!Objects.isNull(authorElement)) {
                            author = authorElement.text();
                        }
                    }
                }
            }
            //            System.out.println(author);
        }
        if (!isValidData(title, publishDate, author)) {
            return null;
        }
        return new CsvItem(url, title, author, publishDate);
    }

    private boolean isValidData(String title, String publishDate, String author) {
        if (Objects.isNull(title) || title.isEmpty()) {
            return false;
        }
        if (Objects.isNull(publishDate) || publishDate.isEmpty()) {
            return false;
        }
        if (Objects.isNull(author) || author.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<String> getLinks() {
        return links;
    }
}
