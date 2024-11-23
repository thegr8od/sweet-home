package com.ssafy.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfo {
    private Long id;
    
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // getter/setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // getter/setter for kakaoAccount
    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    public static class KakaoAccount {
        private Profile profile;
        private boolean profile_needs_agreement;
        private boolean profile_nickname_needs_agreement;
        private boolean profile_image_needs_agreement;

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public static class Profile {
            private String nickname;
            
            @JsonProperty("profile_image_url")
            private String profileImageUrl;
            
            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            // getter/setter for nickname
            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            // getter/setter for profileImageUrl
            public String getProfileImageUrl() {
                return profileImageUrl;
            }

            public void setProfileImageUrl(String profileImageUrl) {
                this.profileImageUrl = profileImageUrl;
            }

            // getter/setter for thumbnailImageUrl
            public String getThumbnailImageUrl() {
                return thumbnailImageUrl;
            }

            public void setThumbnailImageUrl(String thumbnailImageUrl) {
                this.thumbnailImageUrl = thumbnailImageUrl;
            }
        }
    }
}