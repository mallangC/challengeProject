package com.zerobase.challengeproject.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountDetail is a Querydsl query type for AccountDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountDetail extends EntityPathBase<AccountDetail> {

    private static final long serialVersionUID = -136558830L;

    public static final QAccountDetail accountDetail = new QAccountDetail("accountDetail");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> curAmount = createNumber("curAmount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCharge = createBoolean("isCharge");

    public final BooleanPath isRefunded = createBoolean("isRefunded");

    public final NumberPath<Member> member = createNumber("member", Member.class);

    public final NumberPath<Long> preAmount = createNumber("preAmount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccountDetail(String variable) {
        super(AccountDetail.class, forVariable(variable));
    }

    public QAccountDetail(Path<? extends AccountDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountDetail(PathMetadata metadata) {
        super(AccountDetail.class, metadata);
    }

}

