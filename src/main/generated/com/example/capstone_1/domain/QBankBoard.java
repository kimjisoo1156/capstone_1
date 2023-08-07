package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBankBoard is a Querydsl query type for BankBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBankBoard extends EntityPathBase<BankBoard> {

    private static final long serialVersionUID = -561344296L;

    public static final QBankBoard bankBoard = new QBankBoard("bankBoard");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath accountHolder = createString("accountHolder");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath bankName = createString("bankName");

    public final NumberPath<Long> bno = createNumber("bno", Long.class);

    public final StringPath content = createString("content");

    public final SetPath<BoardImage, QBoardImage> imageSetBankBoard = this.<BoardImage, QBoardImage>createSet("imageSetBankBoard", BoardImage.class, QBoardImage.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath isSecret = _super.isSecret;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QBankBoard(String variable) {
        super(BankBoard.class, forVariable(variable));
    }

    public QBankBoard(Path<? extends BankBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBankBoard(PathMetadata metadata) {
        super(BankBoard.class, metadata);
    }

}

