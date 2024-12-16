package filesharing.userhandler.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserCommunication")
public class UserCommunication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long communicationId;

    @ManyToOne
    private MyUser user1;

    @ManyToOne
    private MyUser user2;

    @Column(length = 63, nullable = false)
    private String bucketname;
}
