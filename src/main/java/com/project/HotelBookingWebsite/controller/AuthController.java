package com.project.HotelBookingWebsite.controller;

import com.project.HotelBookingWebsite.dto.LoginDto;
import com.project.HotelBookingWebsite.dto.LoginResponseDTo;
import com.project.HotelBookingWebsite.dto.SignUpDto;
import com.project.HotelBookingWebsite.dto.UserDto;
import com.project.HotelBookingWebsite.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

     @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto)
    {
        return new ResponseEntity<>(authService.signUp(signUpDto),HttpStatus.CREATED);
    }
    @PostMapping("/Login")
    public ResponseEntity<LoginResponseDTo> Login(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        String[] Tokens =authService.login(loginDto);
        Cookie cookie=new Cookie("refreshToken",Tokens[1]);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDTo(Tokens[0]));
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTo> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTo(accessToken));
    }
}
