package com.example.betteriter.fo_domain.search.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSearchWord is a Querydsl query type for SearchWord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchWord extends EntityPathBase<SearchWord> {

    private static final long serialVersionUID = -1260963713L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSearchWord searchWord1 = new QSearchWord("searchWord1");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath searchWord = createString("searchWord");

    public final com.example.betteriter.fo_domain.user.domain.QUsers users;

    public QSearchWord(String variable) {
        this(SearchWord.class, forVariable(variable), INITS);
    }

    public QSearchWord(Path<? extends SearchWord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSearchWord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSearchWord(PathMetadata metadata, PathInits inits) {
        this(SearchWord.class, metadata, inits);
    }

    public QSearchWord(Class<? extends SearchWord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.users = inits.isInitialized("users") ? new com.example.betteriter.fo_domain.user.domain.QUsers(forProperty("users"), inits.get("users")) : null;
    }

}

