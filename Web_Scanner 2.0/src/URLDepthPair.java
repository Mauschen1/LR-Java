public class URLDepthPair {
    private String URL;
    private int sh;
    private int visited;
    public URLDepthPair(String sslk_, int shirina_) {
        URL = sslk_;
        sh = shirina_;
        visited = 1;
    }
    // возвращает ссылку
    public String getURL() {

        return URL;
    }
    // возврщает глубину проверки
    public int getDepth() {

        return sh;
    }
    // увеличивает счетчик посещенных  ссылок
    public void incrementVisited() {

        visited++;
    }
    public String toString() {
        return "<URL href=\"" + URL + "\" visited=\"" + visited + "\" depth=\"" + sh + "\" \\>";
    }
}