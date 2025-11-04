package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audio")
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idaudio;
    @Column(name = "actual")
    private String actual;
    @Column(name = "clasico")
    private String clasico;

    public Audio(int idaudio, String actual, String clasico) {
        this.idaudio = idaudio;
        this.actual = actual;
        this.clasico = clasico;
    }

    public Audio() {
    }

    public int getIdaudio() {
        return idaudio;
    }

    public void setIdaudio(int idaudio) {
        this.idaudio = idaudio;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getClasico() {
        return clasico;
    }

    public void setClasico(String clasico) {
        this.clasico = clasico;
    }

}
