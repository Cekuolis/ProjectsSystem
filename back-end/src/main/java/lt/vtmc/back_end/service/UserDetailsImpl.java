package lt.vtmc.back_end.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lt.vtmc.back_end.domain.User;

public class UserDetailsImpl implements UserDetails {
	
	private String mail;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	

	public UserDetailsImpl(String mail, String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.mail = mail;
		this.password = password;
		this.authorities = authorities;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return mail;
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
		return true;
	}

}
