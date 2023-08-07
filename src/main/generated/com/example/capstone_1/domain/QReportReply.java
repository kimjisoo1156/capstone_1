package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportReply is a Querydsl query type for ReportReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportReply extends EntityPathBase<ReportReply> {

    private static final long serialVersionUID = 1082295652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportReply reportReply = new QReportReply("reportReply");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final BooleanPath isSecret = _super.isSecret;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath replyer = createString("replyer");

    public final StringPath replyText = createString("replyText");

    public final QReportBoard reportBoard;

    public final NumberPath<Long> rno = createNumber("rno", Long.class);

    public QReportReply(String variable) {
        this(ReportReply.class, forVariable(variable), INITS);
    }

    public QReportReply(Path<? extends ReportReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportReply(PathMetadata metadata, PathInits inits) {
        this(ReportReply.class, metadata, inits);
    }

    public QReportReply(Class<? extends ReportReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportBoard = inits.isInitialized("reportBoard") ? new QReportBoard(forProperty("reportBoard")) : null;
    }

}

