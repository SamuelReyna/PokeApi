package risosu.it.PokeApiClient.ML;

public class Abilities {

    private int base_experience;

    private String name;

    private boolean is_hidden;

    private int slot;

    public Abilities(int base_experience, String name, boolean is_hidden, int slot) {
        this.base_experience = base_experience;
        this.name = name;
        this.is_hidden = is_hidden;
        this.slot = slot;
    }

    public Abilities() {
    }

    public int getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(int base_experience) {
        this.base_experience = base_experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(boolean is_hidden) {
        this.is_hidden = is_hidden;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

}
