package version2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleg Cherniak
 * @version 1.0
 * @since 23.03.2017
 */
public class WriteTask implements Runnable {

    private static AtomicInteger fileNumber = new AtomicInteger(0);

    private BlockingQueue<String> queue = null;

    WriteTask(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        File f = null;
        String html;
        try {
            synchronized (queue) {
                if (queue.isEmpty()) {
                    return;
                }
                html = queue.take();
            }
            f = new File(Main2.myPath + "/" + fileNumber.incrementAndGet() + ".html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(html);
            bw.close();
            System.out.println("Created files count = " + Main2.fileCounter.incrementAndGet());
        } catch (IOException e) {
            System.out.println("Problem in writing file:  " + f.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
