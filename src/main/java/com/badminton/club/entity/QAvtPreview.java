package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAvtPreview is a Querydsl query type for AvtPreview
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAvtPreview extends EntityPathBase<AvtPreview> {

    private static final long serialVersionUID = -669937807L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAvtPreview avtPreview = new QAvtPreview("avtPreview");

    public final QActivity activity;

    public final NumberPath<Byte> avtPreCheck = createNumber("avtPreCheck", Byte.class);

    public final NumberPath<Integer> avtPreNo = createNumber("avtPreNo", Integer.class);

    public final QMember member;

    public QAvtPreview(String variable) {
        this(AvtPreview.class, forVariable(variable), INITS);
    }

    public QAvtPreview(Path<? extends AvtPreview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAvtPreview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAvtPreview(PathMetadata metadata, PathInits inits) {
        this(AvtPreview.class, metadata, inits);
    }

    public QAvtPreview(Class<? extends AvtPreview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

