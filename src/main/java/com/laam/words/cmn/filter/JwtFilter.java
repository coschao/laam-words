package com.laam.words.cmn.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.laam.words.app.service.LwUserService;
import com.laam.words.cmn.conf.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//
//	}

    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private LwUserService lwUserService;

    @jakarta.annotation.PostConstruct
    protected void init() {
        log.info("# JwtFilter initialized.");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        // JWT 유효성 검증
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmail(token);

            // 유저 정보 생성
            UserDetails userDetails = lwUserService.loadUserByUsername(email);

            if (userDetails != null) {
                // UserDetails, Password, Role 정보를 기반으로 접근 권한을 가지고 있는 Token 생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Security Context 해당 접근 권한 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 조회 및 Bearer 문자열 제거 후 반환하는 메소드
     * @param request HttpServletRequest
     * @return 추출된 토큰 정보 반환 (토큰 정보가 없을 경우 null 반환)
     */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // Token 정보 존재 여부 및 Bearer 토큰인지 확인
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

	
	
//	//@Autowired
//	private final JwtUtils jwtUtil;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        // Authorization 헤더에서 JWT 토큰 추출
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // "Bearer " 이후의 토큰 값만 추출
//        String token = authorizationHeader.substring(7);
//
//        try {
//            // JWT 만료 여부 검증
//            if (jwtUtil.isTokenExpired(token)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 만료되었습니다.");
//                return;
//            }
//
//            // JWT에서 사용자 정보 추출
//            String username = jwtUtil.getUsername(token);
//            UserRole role = UserRole.valueOf(jwtUtil.getRole(token));
//
//            // 인증 객체 생성
//            User user = User.builder()
//                    .username(username)
//                    .password("N/A") // 비밀번호는 JWT 기반 인증이므로 사용하지 않음
//                    .role(role)
//                    .build();
//
//            CustomUserDetails customUserDetails = new CustomUserDetails(user);
//            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//
//            // SecurityContext에 인증 정보 저장 (STATLESS 모드이므로 요청 종료 시 소멸)
//            if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//
//        } catch (Exception e) {
//            log.error("JWT 필터 처리 중 오류 발생: {}", e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
//        }
//
//        filterChain.doFilter(request, response);
//    }
}
