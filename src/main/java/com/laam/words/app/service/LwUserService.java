package com.laam.words.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.laam.words.app.dao.LwUserDao;
import com.laam.words.cmn.model.LwUserDetails;
import com.laam.words.cmn.model.LwUser;

@Service
public class LwUserService implements UserDetailsService {

//    private final UserRepository userRepository;
	@Autowired
	private LwUserDao lwUserMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LwUser user = null;
        try {
			user = lwUserMapper.findByEmail(email); //.orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
		}
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = null;
		}

        return new LwUserDetails(user);
    }

}
