package com.laam.words.cmn.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import com.laam.words.cmn.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Autowired
    private JwtFilter jwtFilter;

//	@Autowired
//    private JwtProvider jwtProvider;
    //private final ExceptionFilter exceptionFilter;
    
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    
    /**
     * CORS 설정을 위한 Bean 등록
     * - 프론트엔드(React 등)에서 API 요청 시 CORS 문제 해결
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용할 도메인
//            configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
//            configuration.setAllowCredentials(true); // 인증 정보 포함 허용
//            configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
//            configuration.setExposedHeaders(Collections.singletonList("Authorization")); // Authorization 헤더 노출
//            configuration.setMaxAge(3600L); // 1시간 동안 캐싱
//            return configuration;
//        };
        CorsConfiguration config = new CorsConfiguration();

        // 인증정보 주고받도록 허용
        config.setAllowCredentials(true);
        //
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
/*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));// Session 미사용

        // httpBasic, httpFormLogin 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // JWT 관련 필터 설정 및 예외 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(exceptionFilter, JwtFilter.class);

        // 요청 URI별 권한 설정
        http.authorizeHttpRequests((authorize) ->
                // Swagger UI 외부 접속 허용
                authorize.requestMatchers("/*", "/common/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 로그인 로직 접속 허용
                        .requestMatchers("/v1/auth/**").permitAll()
                        // DefaultExceptionHandler 처리를 위한 error PermitAll
                        .requestMatchers("/error/**").permitAll()
                        // 이외의 모든 요청은 인증 정보 필요
                        .anyRequest().authenticated());
 
        return http.build();
    }
*/
//    @Bean
//    public HandlerMappingIntrospector mvcHandlerMappingIntrospector(){
//        return new HandlerMappingIntrospector();
//    }
    
    private static final String[] PERMIT_ALL_PATTERNS = {
    	    "/*",
    	    "/api-docs/**",
    	    "/swagger-ui/**",
    	    "/swagger-ui.html",
    	    "/static/**",
    	    "/common/**"
    	};

    @Bean
    //public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // REST API -> basic auth 및 csrf 보안을 사용하지 않음
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        // JWT를 사용하므로 세션을 사용하지 않음
        // 세션 생성 정책: ALWAYS, NEVER, IF_REQUIRED, STATELESS
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 방식을 사용하므로 폼 로그인, 로그아웃을 사용하지 않음
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        //requestMatchers("/*", "/common/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        // http request 인증 설정
        httpSecurity.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                        // 사용자 삭제는 관리자 권한만 가능
                        .requestMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
                        .requestMatchers("/members/role").hasRole("USER")
                        // 이 밖의 모든 요청에 대해서 인증을 필요로 함
                        .anyRequest().authenticated()
        );

        // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthnticationFilter 전에 실행
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    
    
    

//  private final AuthenticationConfiguration authenticationConfiguration;
//  private final JWTUtil jwtUtil;
//
//  /**
//   * Spring Security의 AuthenticationManager를 빈으로 등록
//   * - 로그인 시 사용자의 인증(Authentication)을 담당
//   */
//  @Bean
//  public AuthenticationManager authenticationManager() throws Exception {
//      return authenticationConfiguration.getAuthenticationManager();
//  }
//
//  /**
//   * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
//   * - 회원가입 시 비밀번호를 안전하게 암호화하여 저장
//   */
//  @Bean
//  public BCryptPasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//  }

//    /**
//     * Spring Security 필터 체인 설정
//     * - JWT 인증을 기반으로 한 보안 설정 적용
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
//                .csrf(csrf -> csrf.disable()) // JWT 사용 시 CSRF 보호 비활성화
//                .formLogin(form -> form.disable()) // 기본 로그인 폼 비활성화 (JWT 사용)
//                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
//
//                // 엔드포인트별 접근 권한 설정
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/", "/signUp").permitAll() // 로그인, 회원가입, 홈은 누구나 접근 가능
//                        .requestMatchers("/admin").hasAuthority("ADMIN") // /admin 경로는 ADMIN 권한이 필요
//                        .anyRequest().authenticated()) // 그 외의 요청은 인증된 사용자만 접근 가능
//
//                // JWT 필터 추가 (기존 UsernamePasswordAuthenticationFilter 이전에 실행)
//                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
//
//                // 로그인 필터 추가 (JWTFilter 실행 후 JWT 발급 처리)
//                .addFilterAfter(new LoginFilter(authenticationManager(), jwtUtil), JWTFilter.class)
//
//                // 세션을 사용하지 않음 (JWT 기반 인증이므로 STATELESS 모드 설정)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
}