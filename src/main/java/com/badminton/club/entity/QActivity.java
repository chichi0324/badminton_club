package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivity is a Querydsl query type for Activity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QActivity extends EntityPathBase<Activity> {

    private static final long serialVersionUID = -670241641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActivity activity = new QActivity("activity");

    public final QActivityType activityType;

    public final ListPath<Advocate, QAdvocate> advocates = this.<Advocate, QAdvocate>createList("advocates", Advocate.class, QAdvocate.class, PathInits.DIRECT2);

    public final DatePath<java.util.Date> avtCutDate = createDate("avtCutDate", java.util.Date.class);

    public final DatePath<java.util.Date> avtDateE = createDate("avtDateE", java.util.Date.class);

    public final DatePath<java.util.Date> avtDateS = createDate("avtDateS", java.util.Date.class);

    public final NumberPath<Byte> avtEdit = createNumber("avtEdit", Byte.class);

    public final NumberPath<Byte> avtFrdData = createNumber("avtFrdData", Byte.class);

    public final DateTimePath<java.sql.Timestamp> avtGatDate = createDateTime("avtGatDate", java.sql.Timestamp.class);

    public final StringPath avtImg = createString("avtImg");

    public final StringPath avtIntr = createString("avtIntr");

    public final StringPath avtLoc = createString("avtLoc");

    public final NumberPath<Integer> avtLow = createNumber("avtLow", Integer.class);

    public final StringPath avtMemo = createString("avtMemo");

    public final ListPath<AvtMessage, QAvtMessage> avtMessages = this.<AvtMessage, QAvtMessage>createList("avtMessages", AvtMessage.class, QAvtMessage.class, PathInits.DIRECT2);

    public final StringPath avtName = createString("avtName");

    public final NumberPath<Integer> avtNo = createNumber("avtNo", Integer.class);

    public final NumberPath<Byte> avtOnly = createNumber("avtOnly", Byte.class);

    public final NumberPath<Byte> avtPre = createNumber("avtPre", Byte.class);

    public final ListPath<AvtPreview, QAvtPreview> avtPreviews = this.<AvtPreview, QAvtPreview>createList("avtPreviews", AvtPreview.class, QAvtPreview.class, PathInits.DIRECT2);

    public final NumberPath<Integer> avtPrice = createNumber("avtPrice", Integer.class);

    public final StringPath avtStat = createString("avtStat");

    public final NumberPath<Integer> avtUpp = createNumber("avtUpp", Integer.class);

    public final QMember member;

    public final ListPath<OtherData, QOtherData> otherData = this.<OtherData, QOtherData>createList("otherData", OtherData.class, QOtherData.class, PathInits.DIRECT2);

    public final ListPath<SignupAvt, QSignupAvt> signupAvts = this.<SignupAvt, QSignupAvt>createList("signupAvts", SignupAvt.class, QSignupAvt.class, PathInits.DIRECT2);

    public QActivity(String variable) {
        this(Activity.class, forVariable(variable), INITS);
    }

    public QActivity(Path<? extends Activity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActivity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActivity(PathMetadata metadata, PathInits inits) {
        this(Activity.class, metadata, inits);
    }

    public QActivity(Class<? extends Activity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activityType = inits.isInitialized("activityType") ? new QActivityType(forProperty("activityType")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

