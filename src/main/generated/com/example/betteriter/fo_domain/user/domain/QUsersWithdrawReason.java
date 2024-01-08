package com.example.betteriter.fo_domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsersWithdrawReason is a Querydsl query type for UsersWithdrawReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsersWithdrawReason extends EntityPathBase<UsersWithdrawReason> {

    private static final long serialVersionUID = -1951659322L;

    public static final QUsersWithdrawReason usersWithdrawReason = new QUsersWithdrawReason("usersWithdrawReason");

    public final com.example.betteriter.global.common.entity.QBaseEntity _super = new com.example.betteriter.global.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> reason = createNumber("reason", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUsersWithdrawReason(String variable) {
        super(UsersWithdrawReason.class, forVariable(variable));
    }

    public QUsersWithdrawReason(Path<? extends UsersWithdrawReason> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsersWithdrawReason(PathMetadata metadata) {
        super(UsersWithdrawReason.class, metadata);
    }

}

