package com.badminton.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystem is a Querydsl query type for System
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSystem extends EntityPathBase<System> {

    private static final long serialVersionUID = 257830327L;

    public static final QSystem system = new QSystem("system");

    public final StringPath sysCon = createString("sysCon");

    public final StringPath sysData = createString("sysData");

    public final StringPath sysName = createString("sysName");

    public final NumberPath<Integer> sysNo = createNumber("sysNo", Integer.class);

    public QSystem(String variable) {
        super(System.class, forVariable(variable));
    }

    public QSystem(Path<? extends System> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystem(PathMetadata metadata) {
        super(System.class, metadata);
    }

}

