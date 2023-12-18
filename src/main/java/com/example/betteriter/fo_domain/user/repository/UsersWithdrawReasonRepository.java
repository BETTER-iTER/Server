package com.example.betteriter.fo_domain.user.repository;

import com.example.betteriter.fo_domain.user.domain.UsersWithdrawReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersWithdrawReasonRepository extends JpaRepository<UsersWithdrawReason, Long> {
}
