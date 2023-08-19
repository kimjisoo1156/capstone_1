package com.example.capstone_1.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBankReply is a Querydsl query type for BankReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBankReply extends EntityPathBase<BankReply> {

    private static final long serialVersionUID = -546851620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBankReply bankReply = new QBankReply("bankReply");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QBankBoard bankBoard;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath replyer = createString("replyer");

    public final StringPath replyText = createString("replyText");

    public final NumberPath<Long> rno = createNumber("rno", Long.class);

    public QBankReply(String variable) {
        this(BankReply.class, forVariable(variable), INITS);
    }

    public QBankReply(Path<? extends BankReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBankReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBankReply(PathMetadata metadata, PathInits inits) {
        this(BankReply.class, metadata, inits);
    }

    public QBankReply(Class<? extends BankReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bankBoard = inits.isInitialized("bankBoard") ? new QBankBoard(forProperty("bankBoard")) : null;
    }

}

