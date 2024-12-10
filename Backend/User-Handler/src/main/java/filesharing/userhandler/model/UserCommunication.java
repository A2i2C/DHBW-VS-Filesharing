package filesharing.userhandler.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCommunication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long communicationId;

    @ManyToOne
    private MyUser user1;

    @ManyToOne
    private MyUser user2;
}
