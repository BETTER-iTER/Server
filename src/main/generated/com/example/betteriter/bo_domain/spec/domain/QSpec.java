package com.example.betteriter.bo_domain.spec.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpec is a Querydsl query type for Spec
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpec extends EntityPathBase<Spec> {

    private static final long serialVersionUID = 278413439L;

    public static final QSpec spec = new QSpec("spec");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final EnumPath<com.example.betteriter.global.constant.Category> category = createEnum("category", com.example.betteriter.global.constant.Category.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<SpecData, QSpecData> specData = this.<SpecData, QSpecData>createList("specData", SpecData.class, QSpecData.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSpec(String variable) {
        super(Spec.class, forVariable(variable));
    }

    public QSpec(Path<? extends Spec> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpec(PathMetadata metadata) {
        super(Spec.class, metadata);
    }

}

