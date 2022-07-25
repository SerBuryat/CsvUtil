package org.thundertech.csv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CsvUtil {

    public static void divideCsv(File file, String quote, String header, int maxSizeBytes,
                                 String pathToSave) throws IOException {

        var newLine = System.lineSeparator();
        var containsTwoQuoteSymbol = "(.?|.+)" + quote + "(.?|.+)" + quote + "(.?|.+)";
        var filePath = Paths.get("");
        int fileCounter = 0;
        var csvLines = Files.lines(Paths.get(file.getAbsolutePath())).skip(1).toList();
        int quoteCounter = 0;
        boolean isOverSized = true;

        for(String line : csvLines) {
            if(isOverSized) {
                fileCounter++;
                filePath = Paths.get(pathToSave + "cuba_session_" + fileCounter + ".csv");
                Files.write(filePath, (header + newLine).getBytes());
                isOverSized = false;
            }

            Files.write(filePath, (line  + newLine).getBytes(),
                    StandardOpenOption.APPEND);

            if(line.contains(quote)) {
                quoteCounter++;
                if(quoteCounter == 2 || line.matches(containsTwoQuoteSymbol)) {
                    isOverSized = Files.size(filePath) > maxSizeBytes;
                    quoteCounter = 0;
                }
            } else {
                if(quoteCounter == 0) {
                    isOverSized = Files.size(filePath) > maxSizeBytes;
                }
            }
        }
    }
}
