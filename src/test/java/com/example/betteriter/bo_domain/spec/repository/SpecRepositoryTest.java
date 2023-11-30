package com.example.betteriter.bo_domain.spec.repository;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.global.constant.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles(profiles = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SpecRepositoryTest {

    @Autowired
    private SpecRepository specRepository;

    @Test
    @DisplayName("카테고리에 해당하는 Spec 리스트를 조회한다.")
    void getSpecsByCategory() {
        // given
        Category category = Category.PC;
        Spec spec01 = Spec.builder().category(category).title("title1").build();
        Spec spec02 = Spec.builder().category(category).title("title2").build();

        this.specRepository.saveAll(List.of(spec01, spec02));
        // when
        List<Spec> result = this.specRepository.findAllByCategory(category);
        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0))
                .extracting("title")
                .isEqualTo("title1");
    }
}
