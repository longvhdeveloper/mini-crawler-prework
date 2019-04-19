package my.vlong.java.webcrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CsvWriter {
    public void writeCsvFile(String fileName, List<CsvItem> data) throws FileNotFoundException {
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            return;
        }
        File csvFile = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(csvFile)) {
            String string = data.stream().map(this::convertToCsv).collect(Collectors.joining("\n"));
            printWriter.write(string);
        }
    }

    private String convertToCsv(CsvItem item) {
        return String.format("%s,%s,%s,%s", this.escapeSpecialCharacters(item.getUrl()),
                this.escapeSpecialCharacters(item.getTitle()), this.escapeSpecialCharacters(item.getPublishDate()),
                this.escapeSpecialCharacters(item.getAuthor()));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
