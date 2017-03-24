package version2;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * @author Oleg Cherniak
 * @version 1.0
 * @since 23.03.2017
 */
public class LoadTask implements Runnable {

    private Element link;

    private BlockingQueue<String> queue = null;

    LoadTask(Element link, BlockingQueue<String> queue) {
        this.link = link;
        this.queue = queue;
    }

    @Override
    public void run() {
        String attr = link.attr("abs:href");
        if (attr.isEmpty()) {
            Main2.latch.countDown();
            return;
        }
        Document doc;
        try {
            doc = Jsoup.connect(attr).get();
            String html = doc.html();
            queue.put(html);

        } catch (HttpStatusException | IllegalArgumentException e) {
            System.out.println("Invalid URL: " + attr);
        } catch (IOException e) {
            System.out.println("Connection problem with link: " + attr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Main2.latch.countDown();
        }
    }
}
