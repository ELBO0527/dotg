package elbo.dotg.api17.domain.user;
import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name="user", indexes = @Index(name = "i_member_name", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String passwd;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profile_url;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(Long id, String username, String passwd, String name, Role role, String profile_url, List<Board> boards) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
        this.name = name;
        this.role = role;
        this.profile_url = profile_url;
        this.boards = boards;
    }

    public void setUserInfo(String username, String name){
        this.username = username;
        this.name = name;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


}
