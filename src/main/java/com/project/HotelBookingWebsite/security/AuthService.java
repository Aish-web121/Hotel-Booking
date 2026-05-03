package com.project.HotelBookingWebsite.security;

import com.project.HotelBookingWebsite.Exception.ResourceNotFoundException;
import com.project.HotelBookingWebsite.dto.LoginDto;
import com.project.HotelBookingWebsite.dto.SignUpDto;
import com.project.HotelBookingWebsite.dto.UserDto;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.entity.enums.Roles;
import com.project.HotelBookingWebsite.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
      public UserDto signUp(SignUpDto user)
      {
         User user1=userRepository.findByEmail(user.getEmail()).orElse(null);
          if(user1!=null)
          {
              throw new RuntimeException("user already exist with email"+user.getEmail());
          }
          User newUser=modelMapper.map(user,User.class);
          newUser.setRoles(Set.of(Roles.Guest));
          newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return modelMapper.map(userRepository.save(newUser),UserDto.class);

      }
      public String[] login(LoginDto loginDto)
      {
          Authentication authentication=authenticationManager.authenticate
                  (new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
          User user=(User)authentication.getPrincipal();
          String[] arr =new String[2];
          arr[0]= jwtService.generateAccessToken(user);
          arr[1]=jwtService.generateRefreshToken(user);
          return arr;
      }
    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }

}
