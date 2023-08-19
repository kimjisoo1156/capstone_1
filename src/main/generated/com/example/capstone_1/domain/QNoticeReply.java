package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeReply is a Querydsl query type for NoticeReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeReply extends EntityPathBase<NoticeReply> {

    private static final long serialVersionUID = 646097472L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeReply noticeReply = new QNoticeReply("noticeReply");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final QNoticeBoard noticeBoard;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath replyer = createString("replyer");

    public final StringPath replyText = createString("replyText");

    public final NumberPath<Long> rno = createNumber("rno", Long.class);

    public QNoticeReply(String variable) {
        this(NoticeReply.class, forVariable(variable), INITS);
    }

    public QNoticeReply(Path<? extends NoticeReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeReply(PathMetadata metadata, PathInits inits) {
        this(NoticeReply.class, metadata, inits);
    }

    public QNoticeReply(Class<? extends NoticeReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.noticeBoard = inits.isInitialized("noticeBoard") ? new QNoticeBoard(forProperty("noticeBoard")) : null;
    }

}

