package com.laam.words.cmn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name = "USER")

@NoArgsConstructor
public class LwUser {

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(nullable = false, unique = true, length = 50)
    private String username;

//    @Column(nullable = false)
    private String password;

    private String email;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private UserRole role; // (USER, ADMIN)
    
    private String role;

    public String getJwtId() {
    	return String.format("%s-%s-%s", this.id, this.email, this.role);
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}