package risosu.it.PokeApiClient.ML;

public class Stats {

    private int base_stat;
    private int effort;
    private String name;
    private String url;

    public Stats(int base_stat, int effort, String name, String url) {
        this.base_stat = base_stat;
        this.effort = effort;
        this.name = name;
        this.url = url;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Stats() {
    }

    public int getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(int base_stat) {
        this.base_stat = base_stat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
