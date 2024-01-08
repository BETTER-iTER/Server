package com.example.betteriter.bo_domain.quiz.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuizAnswer is a Querydsl query type for QuizAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuizAnswer extends EntityPathBase<QuizAnswer> {

    private static final long serialVersionUID = 680368209L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuizAnswer quizAnswer = new QQuizAnswer("quizAnswer");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    public final StringPath answer = createString("answer");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRight = createBoolean("isRight");

    public final QQuiz quiz;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.betteriter.fo_domain.user.domain.QUsers users;

    public QQuizAnswer(String variable) {
        this(QuizAnswer.class, forVariable(variable), INITS);
    }

    public QQuizAnswer(Path<? extends QuizAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuizAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuizAnswer(PathMetadata metadata, PathInits inits) {
        this(QuizAnswer.class, metadata, inits);
    }

    public QQuizAnswer(Class<? extends QuizAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.quiz = inits.isInitialized("quiz") ? new QQuiz(forProperty("quiz")) : null;
        this.users = inits.isInitialized("users") ? new com.example.betteriter.fo_domain.user.domain.QUsers(forProperty("users"), inits.get("users")) : null;
    }

}

