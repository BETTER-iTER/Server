package com.example.betteriter.fo_domain.mypage.dto;

import java.util.List;

import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MypageRequest {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateProfileRequest {
		private String nickname;
		private Job job;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateCategoryRequest {
		private List<Category> categories;
	}
}
