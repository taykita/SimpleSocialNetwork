package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Account account = accountRepository.get(email);
            return account;
        } catch (AccStorageException e) {
            throw new UsernameNotFoundException("UserDetails error: " + e.getMessage());
        }
    }

}
