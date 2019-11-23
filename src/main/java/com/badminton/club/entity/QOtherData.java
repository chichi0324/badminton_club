package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOtherData is a Querydsl query type for OtherData
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOtherData extends EntityPathBase<OtherData> {

    private static final long serialVersionUID = -1453649326L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOtherData otherData = new QOtherData("otherData");

    public final QActivity activity;

    public final ListPath<OtherDataAn, QOtherDataAn> otherDataAns = this.<OtherDataAn, QOtherDataAn>createList("otherDataAns", OtherDataAn.class, QOtherDataAn.class, PathInits.DIRECT2);

    public final StringPath othName = createString("othName");

    public final NumberPath<Integer> othNo = createNumber("othNo", Integer.class);

    public QOtherData(String variable) {
        this(OtherData.class, forVariable(variable), INITS);
    }

    public QOtherData(Path<? extends OtherData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOtherData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOtherData(PathMetadata metadata, PathInits inits) {
        this(OtherData.class, metadata, inits);
    }

    public QOtherData(Class<? extends OtherData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
    }

}

