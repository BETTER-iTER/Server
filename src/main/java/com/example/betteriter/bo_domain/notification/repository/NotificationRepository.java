package com.example.betteriter.bo_domain.notification.repository;

import com.example.betteriter.bo_domain.notification.domain.Notification;
import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByCreatedAtDesc(Users user, Pageable pageable);
}
