package com.laam.words;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.laam.words.cmn.conf.JwtProvider;
import com.laam.words.cmn.model.LwUser;

@SpringBootTest
//@AutoConfigureMockMvc
public class JwtProviderTest {
    //private final JwtProvider jwtProvider = new JwtProvider();
	
	@Autowired
	private JwtProvider jwtProvider;

    @Test
    void test_generateToken() throws Exception {
    	LwUser user = new LwUser();
    	user.setEmail("test@example.com");
    	user.setId(12345670);
    	user.setUsername("Test User");
    	user.setRole("admin");

    	String jwt = jwtProvider.generateToken(user);
    	System.out.println(String.format("JWT Token generated ---\n%s\n", jwt));
    	
    	Thread.sleep(567);
    	boolean isValid = jwtProvider.validateToken(jwt);
    	System.out.println(String.format("JWT Token validation : %s", isValid));

        // then
//        assertEquals(expectedSum, actualSum, "The add method should return the correct sum");
    }
}
