package com.example.betteriter.bo_domain.spec.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpecData is a Querydsl query type for SpecData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpecData extends EntityPathBase<SpecData> {

    private static final long serialVersionUID = -1852420919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpecData specData = new QSpecData("specData");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath data = createString("data");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSpec spec;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSpecData(String variable) {
        this(SpecData.class, forVariable(variable), INITS);
    }

    public QSpecData(Path<? extends SpecData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpecData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpecData(PathMetadata metadata, PathInits inits) {
        this(SpecData.class, metadata, inits);
    }

    public QSpecData(Class<? extends SpecData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.spec = inits.isInitialized("spec") ? new QSpec(forProperty("spec")) : null;
    }

}

