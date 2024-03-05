package com.example.betteriter.fo_domain.review.dto;

import lombok.Builder;

@Builder
public class GetSpecDataDto {
	private Long SpecId;
	private Long SpecDataId;
	private String data;
}
