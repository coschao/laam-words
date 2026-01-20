package com.laam.words.cmn.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.micrometer.common.util.StringUtils;

public class LwUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699514730049421248L;

	private LwUser user;
	
	public LwUserDetails(LwUser user) throws UsernameNotFoundException {
		this.user = user;
		if ( null==user || StringUtils.isBlank(user.getUsername()) ) {
			throw new UsernameNotFoundException("User info is blank!");
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + user.getRole().toString());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
	}

	@Override
	public @Nullable String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

}
