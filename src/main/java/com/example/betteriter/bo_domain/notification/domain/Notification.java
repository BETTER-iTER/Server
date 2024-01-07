package com.example.betteriter.bo_domain.notification.domain;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.RoleType;
import com.example.betteriter.global.constant.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Map;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "NOTIFICATION")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "actor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users actor;

    @Column(name = "actor_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType actorType; // ROLE_USER, ROLE_EXPERTISE, ROLE_ADMIN

    @Column(name = "actor_name", nullable = false)
    private String actorName; // User name

    @JoinColumn(name = "receiver_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users receiver;

    @Column(name = "message_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType; // REVIEW, COMMENT, LIKE

    @Column(name = "message_json", nullable = false)
    @Convert(converter = MessageJson.MessageJsonConverter.class)
    private MessageJson messageJson; // id, title, content, etc

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "status", nullable = false)
    @Enumerated
    private Status status = Status.ACTIVE;
}
