package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdvocate is a Querydsl query type for Advocate
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdvocate extends EntityPathBase<Advocate> {

    private static final long serialVersionUID = 279487731L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdvocate advocate = new QAdvocate("advocate");

    public final QActivity activity;

    public final StringPath advImg = createString("advImg");

    public final NumberPath<Integer> advNo = createNumber("advNo", Integer.class);

    public QAdvocate(String variable) {
        this(Advocate.class, forVariable(variable), INITS);
    }

    public QAdvocate(Path<? extends Advocate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdvocate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdvocate(PathMetadata metadata, PathInits inits) {
        this(Advocate.class, metadata, inits);
    }

    public QAdvocate(Class<? extends Advocate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
    }

}

