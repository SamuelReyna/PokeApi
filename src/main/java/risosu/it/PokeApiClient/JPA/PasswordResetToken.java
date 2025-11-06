package risosu.it.PokeApiClient.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "passwordtoken")
public class PasswordResetToken {

    @Id
    @Column(name = "idpasswordtoken")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "identrenador")
    private int userId;
    @Column(name = "expdate")
    private LocalDateTime expDateTime;

    public PasswordResetToken(String token, int userId, LocalDateTime expDateTime) {
        this.token = token;
        this.userId = userId;
        this.expDateTime = expDateTime;
    }

    public PasswordResetToken() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpDateTime() {
        return expDateTime;
    }

    public void setExpDateTime(LocalDateTime expDateTime) {
        this.expDateTime = expDateTime;
    }

}
