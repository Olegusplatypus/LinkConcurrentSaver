package version1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Oleg Cherniak
 * @version 1.0
 * @since 23.03.2017
 */
public class Main1 {

    public static String myPath;

//    private static final String MAIN_URL = "https://www.google.ru/";
//    private static final String MAIN_URL = "https://www.wikipedia.org/";
    private static final String MAIN_URL = "http://www.arriva.kiev.ua/";

    public static AtomicInteger fileCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {

        int threadsAmount = checkArgument(args);
        createOrCleanFilePath();
        Document doc = Jsoup.connect(MAIN_URL).get();
        Elements links = doc.select("a[href]");
        ExecutorService executor = Executors.newFixedThreadPool(threadsAmount);
        int taskAmount = Math.min(threadsAmount, links.size());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < taskAmount; i++) {
            executor.execute(new LoadAndWriteTask(links.get(i)));
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        long endTime = System.currentTimeMillis();
        System.out.println("Operation time: " + (endTime - startTime) / 1000F + " seconds");
        System.out.println("Test files will be stored in directory: " + myPath);
    }

    private static int checkArgument(String[] args) throws Exception {
        if (args.length == 0) {
            throw new Exception("Please enter threads amount in program arguments");
        }
        String arg = args[0];
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("argument should be numeric.");
        }
    }

    /**
     * method responsible for creation of folder to contain files.
     * Before start cleans folder, if it exist.
     * @throws Exception
     */

    private static void createOrCleanFilePath() throws Exception {
        String projectPath = System.getProperty("user.dir");
        myPath = projectPath + "/test_folder";
        File f;
        if ((f = new File(myPath)).exists()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            boolean mkdir = f.mkdir();
            if (!mkdir) {
                throw new IOException("Directory creation problem.");
            }
        }
    }
}
