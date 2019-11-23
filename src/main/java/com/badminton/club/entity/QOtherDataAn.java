package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOtherDataAn is a Querydsl query type for OtherDataAn
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOtherDataAn extends EntityPathBase<OtherDataAn> {

    private static final long serialVersionUID = -1092628961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOtherDataAn otherDataAn = new QOtherDataAn("otherDataAn");

    public final StringPath otheaCon = createString("otheaCon");

    public final NumberPath<Integer> otheaNo = createNumber("otheaNo", Integer.class);

    public final QOtherData otherData;

    public final QSignupAvt signupAvt;

    public QOtherDataAn(String variable) {
        this(OtherDataAn.class, forVariable(variable), INITS);
    }

    public QOtherDataAn(Path<? extends OtherDataAn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOtherDataAn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOtherDataAn(PathMetadata metadata, PathInits inits) {
        this(OtherDataAn.class, metadata, inits);
    }

    public QOtherDataAn(Class<? extends OtherDataAn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.otherData = inits.isInitialized("otherData") ? new QOtherData(forProperty("otherData"), inits.get("otherData")) : null;
        this.signupAvt = inits.isInitialized("signupAvt") ? new QSignupAvt(forProperty("signupAvt"), inits.get("signupAvt")) : null;
    }

}

