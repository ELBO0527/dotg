package elbo.dotg.api17.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -2123918088L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final elbo.dotg.api17.domain.common.QBaseTimeEntity _super = new elbo.dotg.api17.domain.common.QBaseTimeEntity(this);

    public final ListPath<String, StringPath> attachments = this.<String, StringPath>createList("attachments", String.class, StringPath.class, PathInits.DIRECT2);

    public final elbo.dotg.api17.domain.category.QCategory category;

    public final ListPath<elbo.dotg.api17.domain.comment.Comment, elbo.dotg.api17.domain.comment.QComment> comments = this.<elbo.dotg.api17.domain.comment.Comment, elbo.dotg.api17.domain.comment.QComment>createList("comments", elbo.dotg.api17.domain.comment.Comment.class, elbo.dotg.api17.domain.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    public final elbo.dotg.api17.domain.user.QUser user;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new elbo.dotg.api17.domain.category.QCategory(forProperty("category"), inits.get("category")) : null;
        this.user = inits.isInitialized("user") ? new elbo.dotg.api17.domain.user.QUser(forProperty("user")) : null;
    }

}

