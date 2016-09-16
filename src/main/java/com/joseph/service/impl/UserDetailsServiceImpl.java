package com.joseph.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joseph.dao.UserDao;
import com.joseph.model.Role;
import com.joseph.model.User;
import com.joseph.model.UserStatus;
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userDao.findUserByName(username); //our own User model class
		
		if(user!=null){
			String password = user.getPassword();
			System.out.println("\n\t sword--->"+password);
			//additional information on the security object
			boolean enabled = user.getStatus().equals(UserStatus.ACTIVE);
			System.out.println("\n\t enabled--->"+enabled);

			boolean accountNonExpired = user.getStatus().equals(UserStatus.ACTIVE);
			System.out.println("\n\t accountNonExpired--->"+accountNonExpired);

			boolean credentialsNonExpired = user.getStatus().equals(UserStatus.ACTIVE);
			boolean accountNonLocked = user.getStatus().equals(UserStatus.ACTIVE);
			
			//Let's populate user roles
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(user.getRoles().size());
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for(Role role : user.getRoles()){
				authList.add(new SimpleGrantedAuthority(role.getRoleName()));
				System.out.println("\n\t role.getRoleName()==>"+role.getRoleName());
				authorities.addAll(authList);
			}
			
			//Now let's create Spring Security User object
			org.springframework.security.core.userdetails.User securityUser = new 
					org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
System.out.println(securityUser.getUsername()+"\t "+securityUser.getAuthorities());
			return securityUser;
		}else{
			throw new UsernameNotFoundException("User Not Found!!!");
		}
	}

}
