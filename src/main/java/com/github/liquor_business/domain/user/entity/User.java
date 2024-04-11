package com.github.liquor_business.domain.user.entity;

import com.github.liquor_business.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "social_type")
    private String socialType;

    @Column(name = "social_id")
    private String socialId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<UserRole> roles;

    public User (String socialUserId, String socialName, String email, String userName) {
        this.socialId = socialUserId;
        this.password = "12341234!";
        this.email = email;
        this.socialType = socialName;
        this.userName = userName;
    }

    public User update(String socialUserId, String socialName, String email, String userName) {
        this.socialId = socialUserId;
        this.password = "12341234!";
        this.email = email;
        this.socialType = socialName;
        this.userName = userName;
        return this;
    }


    public User updateUser(UserDto userDto) {
        this.userName = userDto.getUserName();
        return this;
    }
}
