package com.example.betteriter.fo_domain.mypage.dto;

import com.example.betteriter.global.constant.Job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MypageRequest {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public class UpdateProfileRequest {
		private String nickname;
		private Job job;
	}
}
