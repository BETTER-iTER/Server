package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto.GetSpecResponseDto.GetSpecDataResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * - GetReviewSpecDataResponseDto : 해당 카테고리에 대한 Spec (SpecData ) dto
 * - GetSpecResponseDto : 해당 카테고리에 대한 Spec (SpecData
 **/
@Getter
@NoArgsConstructor
public class GetReviewSpecResponseDto {
    private List<GetSpecResponseDto> specs;

    public GetReviewSpecResponseDto(List<GetSpecResponseDto> specs) {
        this.specs = specs;
    }

    public static GetReviewSpecResponseDto from(List<Spec> specs) {
        List<GetSpecResponseDto> getSpecResponses = specs.stream()
                .map(s -> new GetSpecResponseDto(s.getId(), s.getTitle(), GetSpecDataResponseDto.from(s.getSpecData())))
                .collect(Collectors.toList());
        return new GetReviewSpecResponseDto(getSpecResponses);
    }

    @Getter
    @NoArgsConstructor
    static class GetSpecResponseDto { // spec
        private Long id;
        private String title;
        private List<GetSpecDataResponseDto> specData;

        private GetSpecResponseDto(Long id, String title,
                                   List<GetSpecDataResponseDto> specData
        ) {
            this.id = id;
            this.title = title;
            this.specData = specData;
        }

        @Getter
        @NoArgsConstructor
        public static class GetSpecDataResponseDto { // specData
            private Long id;
            private String data;

            private GetSpecDataResponseDto(Long id, String data) {
                this.id = id;
                this.data = data;
            }

            private static List<GetSpecDataResponseDto> from(List<SpecData> specData) {
                return specData.stream()
                        .map(sp -> new GetSpecDataResponseDto(sp.getId(), sp.getData()))
                        .collect(Collectors.toUnmodifiableList());
            }
        }
    }
}
