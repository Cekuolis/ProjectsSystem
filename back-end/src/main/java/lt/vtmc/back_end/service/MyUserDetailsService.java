package lt.vtmc.back_end.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lt.vtmc.back_end.domain.User;
import lt.vtmc.back_end.repos.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		//Empty ArrayList is grantedAuthorities
		User user = userRepo.findByMail(mail);
		return new UserDetailsImpl(user.getMail(), user.getPassword(), new ArrayList<>());
	}

}
