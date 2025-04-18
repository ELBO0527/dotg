package elbo.dotg.api17.domain.user;

import elbo.dotg.api17.domain.board.Board;
import elbo.dotg.api17.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Entity
@Getter
@Table(name = "user", indexes = @Index(name = "i_user_name", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String passwd;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileUrl;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(Long id, String username, String passwd, String name, Role role, String profileurl, List<Board> boards) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
        this.name = name;
        this.role = role;
        this.profileUrl = profileurl;
        this.boards = boards;
    }

    public void updateUserNameAndName(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public void updatePassword(String passwd) {
        this.passwd = passwd;
    }
}