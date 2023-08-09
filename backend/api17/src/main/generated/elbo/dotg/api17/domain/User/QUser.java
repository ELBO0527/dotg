package elbo.dotg.api17.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 441571816L;

    public static final QUser user = new QUser("user");

    public final elbo.dotg.api17.domain.common.QBaseTimeEntity _super = new elbo.dotg.api17.domain.common.QBaseTimeEntity(this);

    public final ListPath<elbo.dotg.api17.domain.board.Board, elbo.dotg.api17.domain.board.QBoard> boards = this.<elbo.dotg.api17.domain.board.Board, elbo.dotg.api17.domain.board.QBoard>createList("boards", elbo.dotg.api17.domain.board.Board.class, elbo.dotg.api17.domain.board.QBoard.class, PathInits.DIRECT2);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath passwd = createString("passwd");

    public final StringPath profile_url = createString("profile_url");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

