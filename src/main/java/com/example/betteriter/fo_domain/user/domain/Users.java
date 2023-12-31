package com.example.betteriter.fo_domain.user.domain;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.global.common.entity.BaseEntity;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.RoleType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@DynamicInsert
@Entity(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_oauth_id", unique = true)
    private String oauthId;

    @Column(name = "usr_email", nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "usr_pwd", unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role", nullable = false)
    private RoleType roleType;

    @Column(name = "usr_is_expert")
    private boolean isExpert;

    /* User 가 관심 등록한 카테고리 리스트 */
    @ElementCollection(targetClass = Category.class)
    @CollectionTable(name = "USERS_CATEGORY",
            joinColumns = @JoinColumn(name = "usr_id"))
    @Enumerated(EnumType.STRING)
    private List<Category> categories;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<ReviewScrap> reviewScraps; // 유저가 스크랩한 리뷰

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes; // 유저가 좋아요한 리뷰

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Review> reviews; // 유저가 작성한 리뷰

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UsersDetail usersDetail; // 유저 상세 정보

    @Builder
    protected Users(Long id, String oauthId, String email, String password, RoleType roleType,
                    boolean isExpert, List<Category> categories,
                    List<ReviewScrap> reviewScraps, List<ReviewLike> reviewLikes,
                    List<Review> reviews, UsersDetail usersDetail
    ) {
        this.id = id;
        this.oauthId = oauthId;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.isExpert = isExpert;
        this.categories = categories;
        this.reviewScraps = reviewScraps;
        this.reviewLikes = reviewLikes;
        this.reviews = reviews;
        this.usersDetail = usersDetail;
    }

    /**
     * 권한 설정
     **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsersCategory(List<Category> categories) {
        this.categories = categories;
    }
}
