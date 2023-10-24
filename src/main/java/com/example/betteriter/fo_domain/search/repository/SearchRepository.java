package com.example.betteriter.fo_domain.search.repository;

import com.example.betteriter.fo_domain.search.domain.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<SearchWord, Long> {

}
