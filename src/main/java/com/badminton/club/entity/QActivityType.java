package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivityType is a Querydsl query type for ActivityType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QActivityType extends EntityPathBase<ActivityType> {

    private static final long serialVersionUID = -131150735L;

    public static final QActivityType activityType = new QActivityType("activityType");

    public final ListPath<Activity, QActivity> activities = this.<Activity, QActivity>createList("activities", Activity.class, QActivity.class, PathInits.DIRECT2);

    public final StringPath avtTypeName = createString("avtTypeName");

    public final NumberPath<Integer> avtTypeNo = createNumber("avtTypeNo", Integer.class);

    public final NumberPath<Byte> avtTypePre = createNumber("avtTypePre", Byte.class);

    public QActivityType(String variable) {
        super(ActivityType.class, forVariable(variable));
    }

    public QActivityType(Path<? extends ActivityType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActivityType(PathMetadata metadata) {
        super(ActivityType.class, metadata);
    }

}

