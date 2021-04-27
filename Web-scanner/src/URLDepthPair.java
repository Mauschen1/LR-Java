public class URLDepthPair {
    private String url;
    private int depth;
    private int visited;
// конструктор
    public URLDepthPair(String url_, int depth_) {
        url = url_;
        depth = depth_;
        visited = 1;
    }
// возвращает УРАЛ М7
    public String getURL() {
        return url;
    }
// возврщает ширину
    public int getDepth() {
        return depth;
    }
// увеличивает счетчик ссылок
    public void incrementVisited() {
        visited++;
    }
// выводит
    public String toString() {
        return "<URL href=\"" + url + "\" visited=\"" + visited + "\" depth=\"" + depth + "\" \\>";
    }
}
