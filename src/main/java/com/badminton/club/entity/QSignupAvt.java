package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSignupAvt is a Querydsl query type for SignupAvt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSignupAvt extends EntityPathBase<SignupAvt> {

    private static final long serialVersionUID = 1584599583L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSignupAvt signupAvt = new QSignupAvt("signupAvt");

    public final QActivity activity;

    public final QMember member;

    public final ListPath<OtherDataAn, QOtherDataAn> otherDataAns = this.<OtherDataAn, QOtherDataAn>createList("otherDataAns", OtherDataAn.class, QOtherDataAn.class, PathInits.DIRECT2);

    public final StringPath signAddr = createString("signAddr");

    public final DatePath<java.util.Date> signBirth = createDate("signBirth", java.util.Date.class);

    public final NumberPath<Integer> signCount = createNumber("signCount", Integer.class);

    public final StringPath signGen = createString("signGen");

    public final StringPath signIdn = createString("signIdn");

    public final StringPath signMail = createString("signMail");

    public final StringPath signMemo = createString("signMemo");

    public final StringPath signName = createString("signName");

    public final NumberPath<Integer> signNo = createNumber("signNo", Integer.class);

    public final StringPath signPhone = createString("signPhone");

    public final DateTimePath<java.sql.Timestamp> signTime = createDateTime("signTime", java.sql.Timestamp.class);

    public final StringPath signUser = createString("signUser");

    public QSignupAvt(String variable) {
        this(SignupAvt.class, forVariable(variable), INITS);
    }

    public QSignupAvt(Path<? extends SignupAvt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSignupAvt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSignupAvt(PathMetadata metadata, PathInits inits) {
        this(SignupAvt.class, metadata, inits);
    }

    public QSignupAvt(Class<? extends SignupAvt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

