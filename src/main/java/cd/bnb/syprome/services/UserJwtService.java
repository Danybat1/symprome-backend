package cd.bnb.syprome.services;


import cd.bnb.syprome.daos.UsersDao;
import cd.bnb.syprome.entities.Users;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserJwtService implements UserDetailsService {
    private UsersDao dao;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
       Users u = this.dao.findByUsername(s);
        if (u != null) {
            return new User(u.getUsername(),passwordEncoder.encode(u.getEmail()),new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found");
    }
    public UserDetails loadUserByCuid(final String s) throws UsernameNotFoundException {
        return loadUserByUsername(s);
    }
    public Users getUser(final String s){
        return dao.findByUsername(s);
    }

}
