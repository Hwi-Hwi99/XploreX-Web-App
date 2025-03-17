import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String email;
    @Column(nullable = false)
    @Getter
    @Setter
    private String password;
    @Column(name = "refresh_token")
    @Getter
    @Setter
    private String refreshToken;

//    public void setEmail(String email) {
//        this.email = email;
//    }
//    public void setPassword(String password) {
//        this.password = password;
//    }
//    public String getEmail() {
//        return email;
//    }
//    public String getPassword() {
//        return password;
//    }
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//    public String getRefreshToken() {
//        return refreshToken;
//    }
}
