package com.laam.words.cmn.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	private final Gson gson = new Gson();
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
//		response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

//        setResponse(response);
		// 필요한 권한이 없이 접근하려 할때 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", "접근 권한이 없습니다.");
        errorDetails.put("status", HttpServletResponse.SC_FORBIDDEN);
        errorDetails.put("path", request.getRequestURI());

        response.getWriter().write(gson.toJson(errorDetails)); // objectMapper.writeValueAsString(errorDetails));
	}

    /**
     * Error 관련 응답 Response 생성 메소드
     * @param response ServletResponse 객체
     * @throws IOException IO 예외 가능성 처리
     */
    private void setResponse(HttpServletResponse response) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(ErrorCode.FORBIDDEN.getHttpStatus().value());
//
//        ExceptionResponse errorResponse = ExceptionResponse.of(ErrorCode.FORBIDDEN);
//        String errorJson = objectMapper.writeValueAsString(errorResponse);
//
//        response.getWriter().write(errorJson);
        // 필요한 권한이 없이 접근하려 할때 403

    }
}
