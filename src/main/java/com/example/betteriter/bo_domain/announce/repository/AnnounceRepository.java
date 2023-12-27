package com.example.betteriter.bo_domain.announce.repository;

import com.example.betteriter.bo_domain.announce.domain.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {

}
