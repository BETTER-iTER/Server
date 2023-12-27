package com.example.betteriter.bo_domain.spec.service;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.repository.SpecDataRepository;
import com.example.betteriter.bo_domain.spec.repository.SpecRepository;
import com.example.betteriter.global.constant.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.betteriter.global.constant.Category.PC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SpecServiceTest {

    @InjectMocks
    private SpecService specService;

    @Mock
    private SpecRepository specRepository;

    @Mock
    private SpecDataRepository specDataRepository;


    @Test
    @DisplayName("카테고리에 해당하는 Spec 리스트를 조회한다.")
    void test() {
        // given
        Category category = PC;
        Spec spec01 = Spec.builder().category(category).title("title1").build();
        Spec spec02 = Spec.builder().category(category).title("title2").build();

        given(this.specRepository.findAllByCategory(any(Category.class)))
                .willReturn(List.of(spec01, spec02));
        // when
        List<Spec> result = this.specService.findAllSpecDataByCategory(category);
        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).extracting("category", "title")
                .containsExactlyInAnyOrder(category, "title1");
    }
}
