package source.controllers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.exception.AccStorageException;

import javax.transaction.Transactional;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    public MyUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Account account = accountRepository.get(email);
            return new User(account.getEmail(), account.getPass(), account.getId(),
                    true, true, true,
                    true, account.getRoles());
        } catch (AccStorageException e) {
            throw new UsernameNotFoundException("UserDetails error: " + e.getMessage());
        }
    }

}
