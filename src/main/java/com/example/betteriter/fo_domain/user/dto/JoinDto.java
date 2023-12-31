package com.example.betteriter.fo_domain.user.dto;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.example.betteriter.global.constant.RoleType.ROLE_USER;

@Getter
@NoArgsConstructor
@ToString(of = {"email", "password", "nickName", "job", "categories"})
public class JoinDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "비밀번호는 영어/숫자/특수문자를 포함해야합니다.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,10}$",
            message = "닉네임은 특수문자 없이 1 ~ 10 글자로 설정 해주세요.")
    private String nickName;

    @NotNull(message = "직업을 선택해야 합니다.")
    private Job job;

    @NotNull(message = "올바른 관심사 입력 형식이 아닙니다.")
    private List<Category> categories;

    @Builder
    public JoinDto(String email, String password, String nickName, Job job, List<Category> categories) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.job = job;
        this.categories = categories;
    }

    public Users toUserEntity(String encryptPassword, UsersDetail usersDetail) {
        return Users.builder()
                .email(email)
                .password(encryptPassword)
                .categories(categories)
                .roleType(ROLE_USER)
                .usersDetail(usersDetail)
                .build();
    }

    public UsersDetail toUserDetailEntity() {
        return UsersDetail.builder()
                .job(job)
                .nickName(nickName)
                .build();
    }

    /* User 가 입력한 관심 카테고리 이름으로 Category List 생성 메소드 */
//    private List<Category> toCategory() {
//        List<String> userCategories = Arrays.asList(this.categories.split(","));
//        return Arrays.stream(Category.values())
//                .filter(category -> userCategories.contains(category.getCategoryName()))
//                .collect(Collectors.toList());
//    }
}
