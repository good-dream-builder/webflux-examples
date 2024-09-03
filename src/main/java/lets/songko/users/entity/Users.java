package lets.songko.users.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection  = "users")
public class Users implements UserDetails {
    @Id
    private ObjectId id;

    @Indexed
    private String userId;

    private String userPassword;

    private String userEmail;

    // 사용자의 권한 목록 (예: ROLE_USER, ROLE_ADMIN)
    private List<String> roles;

    // 계정 활성화 상태
    private boolean enabled;

    // 생성일자
    @CreatedDate
    @Field("created_date")
    private Instant createdDate;

    // 수정일자
    @LastModifiedDate
    @Field("last_modified_date")
    private Instant lastModifiedDate;

    // FIXME only for POC
    private String accessToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 저장된 roles를 SimpleGrantedAuthority로 변환하여 반환
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userId; // 또는 userEmail을 사용할 수도 있음
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // 계정이 활성화되었는지 여부 반환
    }
}
