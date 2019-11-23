package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAvtMessage is a Querydsl query type for AvtMessage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAvtMessage extends EntityPathBase<AvtMessage> {

    private static final long serialVersionUID = 603171760L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAvtMessage avtMessage = new QAvtMessage("avtMessage");

    public final QActivity activity;

    public final QMember member;

    public final StringPath msgCon = createString("msgCon");

    public final NumberPath<Integer> msgNo = createNumber("msgNo", Integer.class);

    public final NumberPath<Integer> msgStar = createNumber("msgStar", Integer.class);

    public final DateTimePath<java.sql.Timestamp> msgTime = createDateTime("msgTime", java.sql.Timestamp.class);

    public QAvtMessage(String variable) {
        this(AvtMessage.class, forVariable(variable), INITS);
    }

    public QAvtMessage(Path<? extends AvtMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAvtMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAvtMessage(PathMetadata metadata, PathInits inits) {
        this(AvtMessage.class, metadata, inits);
    }

    public QAvtMessage(Class<? extends AvtMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

