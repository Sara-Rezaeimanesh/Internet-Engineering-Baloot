package all.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String BirthDate;
    private String address;
    private int credit;

    public User(String username_, String password_, String email_,
                String birthDate_, String address_, int credit_) throws Exception {
        if(!Pattern.matches("^[._a-zA-Z0-9]+$", username_))
            throw new Exception("Username cannot contain especial characters.\n");
        this.username = username_;
        this.password = password_;
        this.email = email_;
        this.address = address_;
        this.credit = credit_;
    }

}