package com.codemountain.blogg.service.serviceImpl;

import com.codemountain.blogg.model.user.User;
import com.codemountain.blogg.repository.UserRepository;
import com.codemountain.blogg.security.UserPrincipal;
import com.codemountain.blogg.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

	private final UserRepository userRepository;
	//private final  UserPrincipal userPrincipal;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) {
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with this username or email: %s", usernameOrEmail)));
		return UserPrincipal.create(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() ->
				new UsernameNotFoundException(String.format("User not found with id: %s", id)));
		return UserPrincipal.create(user);
	}
}
