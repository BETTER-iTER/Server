package com.example.betteriter.bo_domain.menufacturer.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QManufacturer is a Querydsl query type for Manufacturer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManufacturer extends EntityPathBase<Manufacturer> {

    private static final long serialVersionUID = 739840431L;

    public static final QManufacturer manufacturer = new QManufacturer("manufacturer");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final StringPath coName = createString("coName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QManufacturer(String variable) {
        super(Manufacturer.class, forVariable(variable));
    }

    public QManufacturer(Path<? extends Manufacturer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QManufacturer(PathMetadata metadata) {
        super(Manufacturer.class, metadata);
    }

}

