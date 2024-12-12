package filesharing.userhandler.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //Validation for username and password
    @Size(min = 3, max = 8, message = "Username must be between 3 and 8 characters.")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Username must start with a letter and can only contain letters and numbers.")
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 3, message = "Password must be at least 3 characters long.")
    @Column(nullable = false)
    private String password;
}
