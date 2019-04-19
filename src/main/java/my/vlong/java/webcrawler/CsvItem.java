package my.vlong.java.webcrawler;

public class CsvItem {
    private String url;
    private String title;
    private String author;
    private String publishDate;

    public CsvItem(String url, String title, String author, String publishDate) {
        this.url = url;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishDate() {
        return publishDate;
    }
}
