package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardImage is a Querydsl query type for BoardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardImage extends EntityPathBase<BoardImage> {

    private static final long serialVersionUID = 1918136583L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardImage boardImage = new QBoardImage("boardImage");

    public final QBankBoard bankBoard;

    public final StringPath fileName = createString("fileName");

    public final QFreeBoard freeBoard;

    public final QNoticeBoard noticeBoard;

    public final NumberPath<Integer> ord = createNumber("ord", Integer.class);

    public final QReportBoard reportBoard;

    public final StringPath uuid = createString("uuid");

    public QBoardImage(String variable) {
        this(BoardImage.class, forVariable(variable), INITS);
    }

    public QBoardImage(Path<? extends BoardImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardImage(PathMetadata metadata, PathInits inits) {
        this(BoardImage.class, metadata, inits);
    }

    public QBoardImage(Class<? extends BoardImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bankBoard = inits.isInitialized("bankBoard") ? new QBankBoard(forProperty("bankBoard")) : null;
        this.freeBoard = inits.isInitialized("freeBoard") ? new QFreeBoard(forProperty("freeBoard")) : null;
        this.noticeBoard = inits.isInitialized("noticeBoard") ? new QNoticeBoard(forProperty("noticeBoard")) : null;
        this.reportBoard = inits.isInitialized("reportBoard") ? new QReportBoard(forProperty("reportBoard")) : null;
    }

}

