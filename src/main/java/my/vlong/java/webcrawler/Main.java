package my.vlong.java.webcrawler;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Crawler crawler = new Crawler();
        crawler.search("https://vnexpress.net/");
    }
}
