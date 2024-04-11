package com.github.liquor_business.domain.oauth.service;

import com.github.liquor_business.domain.oauth.entity.CustomOAuth2User;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        // 카카오, 네이버 정보 가지고 오기
        OAuth2User oAuth2User = super.loadUser(request);
        // 카카오인지, 네이버인지 구분하기 위해서
        // 클라이언트의 이름 가지고 오기
        String oauthClientName = request.getClientRegistration().getClientName();

        // 카카오, 네이버 정보를 가지고 User 엔티티 만들고 저장하기
        User user = null;
        String socialUserId = null;
        String email = "email@email.com";
        String userName = null;

        if (oauthClientName.equals("kakao")) {
            socialUserId = "kakao_" + oAuth2User.getAttributes().get("id");
            user = new User(socialUserId, oauthClientName, email, userName);
        }

//        if(oauthClientName.equals("naver")) {
//            @SuppressWarnings("unchecked")
//            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
//            socialUserId = "naver_" + responseMap.get("id").substring(0, 14);
//            email = responseMap.get("email");
//            userName = responseMap.get("name");
//            user = new User(socialUserId, oauthClientName, email, userName);
//        }

        saveOrUpdate(user);
        // 토근을 발행하가 위해서 OAuth2User를 구현해준다.
        // 그리고 생성해서 반환한다.
        return new CustomOAuth2User(socialUserId, email);
    }

    private void saveOrUpdate(User user) {
        User findUser = userRepository.findByEmailAndSocialType(user.getEmail(), user.getSocialType())
                .map(m -> m.update(user.getSocialId(), user.getSocialType(), user.getEmail(), user.getUserName()))
                .orElse(user);

        log.info("findUser = {}" , findUser);

        userRepository.save(findUser);
    }
}
