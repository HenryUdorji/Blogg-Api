package com.codemountain.blogg.security;

import com.codemountain.blogg.exception.UnauthorizedException;
import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.stream.Collectors;


public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;


	private Long id;

	private String firstName;

	private String lastName;

	private String username;

	@JsonIgnore
	private String email;

	@JsonIgnore
	private String password;

	@JsonIgnore
	private Boolean userIsEnabled;

	private Collection<? extends GrantedAuthority> authorities;


	public UserPrincipal(Long id, String firstName, String lastName, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities, Boolean userIsEnabled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.userIsEnabled = userIsEnabled;

		if (authorities == null) {
			this.authorities = null;
		} else {
			this.authorities = new ArrayList<>(authorities);
		}
	}

	public static UserPrincipal create(User user) {
		return new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
				user.getEmail(), user.getPassword(), getAuthorities("USER"), user.getUserIsEnabled());
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
		return userIsEnabled;
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) object;
		return Objects.equals(id, that.id);
	}

	public int hashCode() {
		return Objects.hash(id);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
