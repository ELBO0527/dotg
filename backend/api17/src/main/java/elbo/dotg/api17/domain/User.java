package elbo.dotg.api17.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name="user", indexes = @Index(name = "i_member_name", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String passwd;
    private String name;
    private Role role;
    private String profile_url;

    @Builder
    public User(Long id, String username, String passwd, String name, Role role, String profile_url) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
        this.name = name;
        this.role = role;
        this.profile_url = profile_url;
    }

    public void setUserInfo(String username, String name){
        this.username = username;
        this.name = name;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


}
