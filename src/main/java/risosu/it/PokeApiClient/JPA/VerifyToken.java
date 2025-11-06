package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "verifytoken")
public class VerifyToken {

    @Id
    @Column(name = "idverifytoken")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idverify;
    @Column(name = "token")
    private String token;
    @Column(name = "identrenador")
    private int userId;
    @Column(name = "expdate")
    private LocalDateTime expDate;

    public VerifyToken(int idverify, String token, int userId, LocalDateTime expDate) {
        this.idverify = idverify;
        this.token = token;
        this.userId = userId;
        this.expDate = expDate;
    }

    public VerifyToken() {
    }

    public int getIdVerify() {
        return idverify;
    }

    public void setIdVerify(int idverify) {
        this.idverify = idverify;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIduser() {
        return userId;
    }

    public void setIduser(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDateTime expDate) {
        this.expDate = expDate;
    }

}
