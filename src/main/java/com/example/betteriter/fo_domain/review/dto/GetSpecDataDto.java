package com.example.betteriter.fo_domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetSpecDataDto {
	private Long SpecId;
	private Long SpecDataId;
	private String data;
}
