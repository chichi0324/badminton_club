package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 67388962L;

    public static final QMember member = new QMember("member1");

    public final ListPath<Activity, QActivity> activities = this.<Activity, QActivity>createList("activities", Activity.class, QActivity.class, PathInits.DIRECT2);

    public final ListPath<AvtMessage, QAvtMessage> avtMessages = this.<AvtMessage, QAvtMessage>createList("avtMessages", AvtMessage.class, QAvtMessage.class, PathInits.DIRECT2);

    public final ListPath<AvtPreview, QAvtPreview> avtPreviews = this.<AvtPreview, QAvtPreview>createList("avtPreviews", AvtPreview.class, QAvtPreview.class, PathInits.DIRECT2);

    public final StringPath memAddr = createString("memAddr");

    public final DatePath<java.util.Date> memBirth = createDate("memBirth", java.util.Date.class);

    public final StringPath memGen = createString("memGen");

    public final StringPath memIdn = createString("memIdn");

    public final StringPath memImg = createString("memImg");

    public final NumberPath<Byte> memInfo = createNumber("memInfo", Byte.class);

    public final StringPath memMail = createString("memMail");

    public final StringPath memName = createString("memName");

    public final StringPath memPhone = createString("memPhone");

    public final DateTimePath<java.sql.Timestamp> memTime = createDateTime("memTime", java.sql.Timestamp.class);

    public final StringPath memUser = createString("memUser");

    public final ListPath<SignupAvt, QSignupAvt> signupAvts = this.<SignupAvt, QSignupAvt>createList("signupAvts", SignupAvt.class, QSignupAvt.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

