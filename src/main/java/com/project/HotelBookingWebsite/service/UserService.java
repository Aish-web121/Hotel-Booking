package com.project.HotelBookingWebsite.service;

import com.project.HotelBookingWebsite.Exception.ResourceNotFoundException;
import com.project.HotelBookingWebsite.entity.User;
import com.project.HotelBookingWebsite.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User getUserById(Long id)
    {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found with id"+id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}
