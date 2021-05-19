import java.io.*; // реализация потоков
import java.net.*; // пакет для сокетов
import java.util.*; // пакет для списков
import java.util.regex.*; // для регулярных выражений


public class Crawler {
    // Главный поток
    public static void main(String[] args) {
        if (args.length != 2) showHelp();

        String url = args[0];
        int depth = 0;

        try {
            depth = Integer.parseInt(args[1]);
        } catch (Exception e) {
            showHelp();
        }

        Crawler crawler = new Crawler(url, depth);
        crawler.run();
    }

    private HashMap<String, URLDepthPair> links = new HashMap<>();
    private LinkedList<URLDepthPair> pool = new LinkedList<>();

    private int depth = 0;
// Это конструктор
    public Crawler(String url, int depth_) {
        depth = depth_;
        pool.add(new URLDepthPair(url, 0));
    }
// Запуск потока. Поток - это процесс программы, ее работа
    public void run() {
        while (pool.size() > 0 || Thread.activeCount() > 1) {
            if (pool.size() > 0) {
                URLDepthPair link = pool.pop();
                CrawlerThread task = new CrawlerThread(link);
                task.start();
            }
        }

        for (URLDepthPair link : links.values())
            System.out.println(link);

        System.out.println();
        System.out.printf("Found %d URLS\n", links.size());
    }
// Регулярное выражение
    public static Pattern LINK_REGEX = Pattern.compile(
            "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1"
    );
// Вложенный класс CrawlerThread (Thread - класс, представляющий в Java поток)
    private class CrawlerThread extends Thread {
        private URLDepthPair link;

        public CrawlerThread(URLDepthPair link_) {
            link = link_;
        }

        @Override
        // Опять запуск потока
        public void run() {
            // Создание дерева ссылок. Проверка на наличие ссылок на странице, добавление прошлой ссылки в запись,
            // переход на новую ссылку
            if (links.containsKey(link.getURL())) {
                URLDepthPair knownLink = links.get(link.getURL());
                knownLink.incrementVisited();
                return;
            }

            links.put(link.getURL(), link);

            if (link.getDepth() >= depth)
                return;
// Добавление новой ссылки
            try {
                URL url = new URL(link.getURL());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                Scanner s = new Scanner(con.getInputStream());
                while (s.findWithinHorizon(LINK_REGEX, 0) != null) {
                    String newURL = s.match().group(2);
                    if (newURL.startsWith("/"))
                        newURL = link.getURL() + newURL;
                    else if (!newURL.startsWith("http"))
                        continue;
                    URLDepthPair newLink = new URLDepthPair(newURL, link.getDepth() + 1);
                    pool.add(newLink);
                }
            } catch (Exception e) {}
        }
    }
// Помощь (выводит сообщение, когда пользоваьель неправильно ввел данные для работы)
    public static void showHelp() {
        System.out.println("usage: java Crawler <URL> <depth>");
        System.exit(1);
    }

}