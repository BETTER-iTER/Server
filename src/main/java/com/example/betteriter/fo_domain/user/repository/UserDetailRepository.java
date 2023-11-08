package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UsersDetail, Long> {
    int countByNickName(String nickname);
}
