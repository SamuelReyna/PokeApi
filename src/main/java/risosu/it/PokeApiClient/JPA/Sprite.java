package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sprite")
public class Sprite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsprite")
    private int idsprite;
    @Column(name = "defaulttrasero")
    private String defaulttrasero;
    @Column(name = "defaultfrontal")
    private String defaultfrontal;
    @Column(name = "brillantetrasero")
    private String brillantetrasero;
    @Column(name = "brillantefrontal")
    private String brillantefrontal;

    public int getIdsprite() {
        return idsprite;
    }

    public void setIdsprite(int idsprite) {
        this.idsprite = idsprite;
    }

    public String getDefaulttrasero() {
        return defaulttrasero;
    }

    public void setDefaulttrasero(String defaulttrasero) {
        this.defaulttrasero = defaulttrasero;
    }

    public String getDefaultfrontal() {
        return defaultfrontal;
    }

    public void setDefaultfrontal(String defaultfrontal) {
        this.defaultfrontal = defaultfrontal;
    }

    public String getBrillantetrasero() {
        return brillantetrasero;
    }

    public void setBrillantetrasero(String brillantetrasero) {
        this.brillantetrasero = brillantetrasero;
    }

    public String getBrillantefrontal() {
        return brillantefrontal;
    }

    public void setBrillantefrontal(String brillantefrontal) {
        this.brillantefrontal = brillantefrontal;
    }
    
    
}
