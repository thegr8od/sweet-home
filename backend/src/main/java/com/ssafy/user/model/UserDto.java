package com.ssafy.user.model;

public class UserDto {

	  private String id;
	    private String password;
	    private String name;
	    private int age;
	    private String phoneNumber;
	    private String email;
	    private String joinDate;
	    private boolean isDeleted;
	    private String deletedAt;
	    private String profileImage;
	    private String profileImageUrl; // 프로필 이미지 조회를 위한 URL


	    public UserDto() {
	    }

	    public UserDto(String id, String password, String name, String phoneNumber, String email, String joinDate) {
	        this.id = id;
	        this.password = password;
	        this.name = name;
	        this.phoneNumber = phoneNumber;
	        this.email = email;
	        this.joinDate = joinDate;
	    }
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	// age 필드의 setter
	public void setAge(int age) {
		this.age = age;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", password=" + password + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + "]";
	}
	
	public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
