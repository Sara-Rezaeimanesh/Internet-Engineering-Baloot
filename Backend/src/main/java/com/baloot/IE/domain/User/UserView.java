package com.baloot.IE.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserView {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private String birthDate;
    @NonNull
    private String address;
    @JsonIgnore
    private int credit;


    public User viewToUser() throws Exception {
        return new User(username, password, email, birthDate, address, credit);
    }
}
