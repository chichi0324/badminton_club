package com.badminton.club;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.badminton.club.dao.UserRepository;
import com.badminton.club.entity.Authority;

@Service
public class ClubUserDetailsService implements UserDetailsService {
    
	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException{
        // TODO Auto-generated method stub
        com.badminton.club.entity.User user=userRepository.findByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("Invalid username/password");
        }
        //用集合及一個helper class來取得user的擁有的role
        Collection<? extends GrantedAuthority> authorities=  UserAuthorityUtils.createAuthorities(user);
                //UserAuthorityUtils是helper class
        System.out.println("-------data:"+user.getUsername()+","+user.getPassword());
        return new User(user.getUsername(), user.getPassword(), authorities); 
        //這邊的User是指org.springframework.security.core.userdetails.User
        //是UserDetails介面的實作，儲存使用者名稱、密碼及擁有權限
    }
}
