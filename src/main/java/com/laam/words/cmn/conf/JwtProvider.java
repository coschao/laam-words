package com.laam.words.cmn.conf;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.laam.words.cmn.model.LwUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    
    private java.security.Key key;
    
    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @jakarta.annotation.PostConstruct
    protected void init() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
    	//byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(secretKeyBytes);
//        System.out.println("############## Key initialized! secretKey = " + secretKey);
//        System.out.println("############## Key algorithm = " + key.getAlgorithm());
//        System.out.println("############## Key getEncoded = " + key.getEncoded());
        log.info("# JwtProvider : expirationTime : {}", expirationTime);
    }

//    public static String generateToken(String subject, long ttlMillis) {
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        Date expirationDate = new Date(nowMillis + ttlMillis);
//
//        return Jwts.claims().
//                .setSubject(subject) // 페이로드 'sub' (subject) 클레임
//                .setIssuedAt(now)    // 페이로드 'iat' (issued at) 클레임
//                .setExpiration(expirationDate) // 페이로드 'exp' (expiration) 클레임
//                .signWith(secretKey, SignatureAlgorithm.HS256) // 서명에 사용할 키와 알고리즘 지정
//                .compact(); // JWT 문자열 생성
//    }
    
    /**
     * JWT 생성
     * @param user 사용자 정보
     * @return 사용자 정보를 기반으로 추출된 토큰 반환
     */
    public String generateToken(LwUser user) {
        Claims claims = getClaims(user);
        System.out.println("############## claims = " + claims);

        Date now = new Date();
//        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
//        key = Keys.hmacShaKeyFor(secretKeyBytes);

//        System.out.println("############## Key algorithm = " + key.getAlgorithm());
//        System.out.println("############## Key format = " + key.getFormat());
        return Jwts.builder().claims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(this.key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            //Jws<Claims> claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
        	//return claims.getBody().getExpiration().after(new Date());
        	//Jws<Claims> claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
        	Jws<Claims> claims = Jwts.parser().verifyWith((javax.crypto.SecretKey)key)
        			   				// .setSigningKey(key) // 이 방식도 여전히 동작합니다.
        			   				// .requireIssuer("your_issuer") // 발행자 검증 (옵션)
        			   				// .requireAudience("your_audience") // 수신자 검증 (옵션)
        			   				.build() // JwtParser 객체 생성
        			   				.parseSignedClaims(token);
        			   				//.parseClaimsJws(token); // 토큰 파싱 및 서명 검증
        	System.out.println("############## claims2 = " + claims);
            return claims.getPayload().getExpiration().after(new Date());
        }
        catch (Exception e) {
        	e.printStackTrace();
            return false; //throw new RuntimeException(e);
        }
    }

    public String getEmail(String token) {
        //return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    	return (String) Jwts.parser().build().parseSignedClaims(token).getPayload().get("email");
    }

    /**
     * 토큰의 만료기한 반환
     * @param token 일반적으로 액세스 토큰 / 토큰 재발급 요청 시에는 리프레쉬 토큰이 들어옴
     * @return 해당 토큰의 만료정보를 반환
     */
    public Long getExpirationTime(String token) {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().getTime();
    }

    /**
     * Claims 정보 생성
     * @param user 사용자 정보 중 사용자를 구분할 수 있는 정보 두 개를 활용함
     * @return 사용자 구분 정보인 이메일과 역할을 저장한 Claims 객체 반환
     */
    private Claims getClaims(LwUser user) {
        //return Jwts.claims().setSubject(user.getSecret()); //.setSubject(user.);
    	return Jwts.claims().add("id", user.getId())
    			.add("email", user.getEmail())
    			.add("role", user.getRole())
    			.issuedAt(new Date())
    			.expiration(new Date(System.currentTimeMillis() + this.expirationTime))
    			.issuer("LAAM-WORDS-JWT")
    			.audience().add(user.getEmail()).and()
    			.id(user.getJwtId())
    			.build();
    }
}
