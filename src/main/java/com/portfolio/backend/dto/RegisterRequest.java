package com.portfolio.backend.dto;

public class RegisterRequest {

	    private String name;
	    private String email;
	    private String username;
	    private String password;
	    private String confirmPassword;

	    public RegisterRequest() {}

	    public String getName() { return name; }
	    public String getEmail() { return email; }
	    public String getUsername() { return username; }
	    public String getPassword() { return password; }
	    public String getConfirmPassword() { return confirmPassword; }

	    public void setName(String name) { this.name = name; }
	    public void setEmail(String email) { this.email = email; }
	    public void setUsername(String username) { this.username = username; }
	    public void setPassword(String password) { this.password = password; }
	    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}

