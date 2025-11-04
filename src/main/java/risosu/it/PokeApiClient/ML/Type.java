package risosu.it.PokeApiClient.ML;

public class Type {

    private int slot;
    private String name;
    private String url;

    public Type() {
    }

    public Type(int slot, String name, String url) {
        this.slot = slot;
        this.name = name;
        this.url = url;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
