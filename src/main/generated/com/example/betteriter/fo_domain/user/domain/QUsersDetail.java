package com.example.betteriter.fo_domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsersDetail is a Querydsl query type for UsersDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsersDetail extends EntityPathBase<UsersDetail> {

    private static final long serialVersionUID = 1949753641L;

    public static final QUsersDetail usersDetail = new QUsersDetail("usersDetail");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.betteriter.global.constant.Job> job = createEnum("job", com.example.betteriter.global.constant.Job.class);

    public final StringPath nickName = createString("nickName");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Integer> quizCount = createNumber("quizCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUsersDetail(String variable) {
        super(UsersDetail.class, forVariable(variable));
    }

    public QUsersDetail(Path<? extends UsersDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsersDetail(PathMetadata metadata) {
        super(UsersDetail.class, metadata);
    }

}

