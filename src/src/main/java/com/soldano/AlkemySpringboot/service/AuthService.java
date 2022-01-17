package com.soldano.AlkemySpringboot.service;

import com.soldano.AlkemySpringboot.dto.user.LoginResponse;
import com.soldano.AlkemySpringboot.dto.user.UserDto;
import com.soldano.AlkemySpringboot.exceptions.UniqueException;
import com.soldano.AlkemySpringboot.mapper.ClassMapper;
import com.soldano.AlkemySpringboot.repository.UserRepository;
import com.soldano.AlkemySpringboot.security.CustomUserDetailService;
import com.soldano.AlkemySpringboot.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final ClassMapper classMapper;
    private final EmailService emailService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, CustomUserDetailService customUserDetailService, UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, ClassMapper classMapper, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailService = customUserDetailService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.classMapper = classMapper;
        this.emailService = emailService;
    }

    public LoginResponse authUser(UserDto user) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
        return new LoginResponse(jwtService.generateToken(customUserDetailService.loadUserByUsername(user.getUsername())));
    }

    public void registerUser(UserDto user) throws UniqueException {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new UniqueException("username", user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(classMapper.userDtoToUser(user));
        emailService.sendEmail(user.getUsername());
    }
}
