package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeReply is a Querydsl query type for FreeReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeReply extends EntityPathBase<FreeReply> {

    private static final long serialVersionUID = 806896300L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeReply freeReply = new QFreeReply("freeReply");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QFreeBoard freeBoard;

    //inherited
    public final BooleanPath isSecret = _super.isSecret;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath replyer = createString("replyer");

    public final StringPath replyText = createString("replyText");

    public final NumberPath<Long> rno = createNumber("rno", Long.class);

    public QFreeReply(String variable) {
        this(FreeReply.class, forVariable(variable), INITS);
    }

    public QFreeReply(Path<? extends FreeReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeReply(PathMetadata metadata, PathInits inits) {
        this(FreeReply.class, metadata, inits);
    }

    public QFreeReply(Class<? extends FreeReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.freeBoard = inits.isInitialized("freeBoard") ? new QFreeBoard(forProperty("freeBoard")) : null;
    }

}

