
package risosu.it.PokeApiClient.ML;


public class Species {

    private String name;
    private String url;
    public SpeciesDetail speciesDetail;

    public SpeciesDetail getSpeciesDetail() {
        return speciesDetail;
    }

    public void setSpeciesDetail(SpeciesDetail speciesDetail) {
        this.speciesDetail = speciesDetail;
    }

    // Getters y Setters
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
