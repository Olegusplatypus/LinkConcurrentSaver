package version1;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleg Cherniak
 * @version 1.0
 * @since 23.03.2017
 */
public class LoadAndWriteTask implements Runnable {

    private static AtomicInteger fileNumber = new AtomicInteger(0);

    private Element link;

    LoadAndWriteTask(Element link) {
        this.link = link;
    }

    @Override
    public void run() {
        String attr = link.attr("abs:href");
        if (attr.isEmpty()) {
            return;
        }
        try {
            Document doc = Jsoup.connect(attr).get();
            String html = doc.html();
            File f = new File(Main1.myPath + "/" + fileNumber.incrementAndGet() + ".html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(html);
            bw.close();
            System.out.println("Created files count = " + Main1.fileCounter.incrementAndGet());
        } catch (HttpStatusException | IllegalArgumentException e) {
            System.out.println("Invalid URL: " + attr);
        } catch (IOException e) {
            System.out.println("Problem in writing file:  " + attr);
        }
    }
}
