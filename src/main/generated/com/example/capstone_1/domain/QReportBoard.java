package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportBoard is a Querydsl query type for ReportBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportBoard extends EntityPathBase<ReportBoard> {

    private static final long serialVersionUID = 1067802976L;

    public static final QReportBoard reportBoard = new QReportBoard("reportBoard");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> bno = createNumber("bno", Long.class);

    public final StringPath content = createString("content");

    public final ListPath<FileEntity, QFileEntity> files = this.<FileEntity, QFileEntity>createList("files", FileEntity.class, QFileEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath secret = createString("secret");

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QReportBoard(String variable) {
        super(ReportBoard.class, forVariable(variable));
    }

    public QReportBoard(Path<? extends ReportBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportBoard(PathMetadata metadata) {
        super(ReportBoard.class, metadata);
    }

}

