package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    int countByNickName(String nickname);
}
