package com.baloot.IE.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserView {
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String birthDate;
    private String address;
    @NonNull
    private int credit;

    public User viewToUser() throws Exception {
        return new User(username, password, email, birthDate, address, credit);
    }
}
