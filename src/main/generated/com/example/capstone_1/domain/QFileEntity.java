package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileEntity is a Querydsl query type for FileEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileEntity extends EntityPathBase<FileEntity> {

    private static final long serialVersionUID = 1577189233L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileEntity fileEntity = new QFileEntity("fileEntity");

    public final EnumPath<BoardType> boardType = createEnum("boardType", BoardType.class);

    public final StringPath fileName = createString("fileName");

    public final QFreeBoard freeBoard;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QNoticeBoard noticeBoard;

    public final QReportBoard reportBoard;

    public final StringPath s3Url = createString("s3Url");

    public final StringPath uuid = createString("uuid");

    public QFileEntity(String variable) {
        this(FileEntity.class, forVariable(variable), INITS);
    }

    public QFileEntity(Path<? extends FileEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileEntity(PathMetadata metadata, PathInits inits) {
        this(FileEntity.class, metadata, inits);
    }

    public QFileEntity(Class<? extends FileEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.freeBoard = inits.isInitialized("freeBoard") ? new QFreeBoard(forProperty("freeBoard")) : null;
        this.noticeBoard = inits.isInitialized("noticeBoard") ? new QNoticeBoard(forProperty("noticeBoard")) : null;
        this.reportBoard = inits.isInitialized("reportBoard") ? new QReportBoard(forProperty("reportBoard")) : null;
    }

}

