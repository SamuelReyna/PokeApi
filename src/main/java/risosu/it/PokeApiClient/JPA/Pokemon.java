/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokemon")
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPokemon")
    private int idPokemon;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "alto")
    private int altura;
    
    @Column(name = "ancho")
    private int ancho;
    
    @Column(name = "enFormaBase")
    private int enFormaBase;
    
    @OneToOne
    @JoinColumn(name = "idSprite")
    @Column(name = "idSprite")
    private Sprite sprite;
    
    @OneToOne
    @JoinColumn(name = "idAudio")
    private Audio audio;
    
    
}
