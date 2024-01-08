package com.example.betteriter.bo_domain.announce.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnnounce is a Querydsl query type for Announce
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnnounce extends EntityPathBase<Announce> {

    private static final long serialVersionUID = 802561371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnnounce announce = new QAnnounce("announce");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.betteriter.fo_domain.user.domain.QUsers writerId;

    public QAnnounce(String variable) {
        this(Announce.class, forVariable(variable), INITS);
    }

    public QAnnounce(Path<? extends Announce> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnnounce(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnnounce(PathMetadata metadata, PathInits inits) {
        this(Announce.class, metadata, inits);
    }

    public QAnnounce(Class<? extends Announce> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writerId = inits.isInitialized("writerId") ? new com.example.betteriter.fo_domain.user.domain.QUsers(forProperty("writerId"), inits.get("writerId")) : null;
    }

}

