package com.iscas.sdas.dto.sys;

import com.iscas.sdas.common.BaseDto;

public class UserDto extends BaseDto{
	
    private String userId;
    
    private String rolename;

    private String username;

    private String password;

    private String email;

    private String mobile;

    private Integer userLocked;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getUserLocked() {
        return userLocked;
    }

    public void setUserLocked(Integer userLocked) {
        this.userLocked = userLocked;
    }

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}