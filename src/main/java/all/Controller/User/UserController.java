package all.Controller.User;

import all.domain.User.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return null;
    }

}
