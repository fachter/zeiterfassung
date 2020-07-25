package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.Authenticate;
import com.fhws.zeiterfassung.boundaries.useCases.GetAccountInformation;
import com.fhws.zeiterfassung.viewModels.AccountInformationViewModel;
import com.fhws.zeiterfassung.viewModels.AuthenticationRequest;
import com.fhws.zeiterfassung.viewModels.AuthenticationResponse;
import com.fhws.zeiterfassung.services.UserRepositoryUserDetailsService;
import com.fhws.zeiterfassung.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUseCase implements Authenticate {

    private final AuthenticationManager authenticationManager;
    private final UserRepositoryUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final GetAccountInformation getAccountInformation;

    @Autowired
    public AuthenticateUseCase(AuthenticationManager authenticationManager,
                               UserRepositoryUserDetailsService userDetailsService,
                               JwtUtil jwtTokenUtil,
                               GetAccountInformation getAccountInformation) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.getAccountInformation = getAccountInformation;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        final AccountInformationViewModel accountInfo = getAccountInformation.getViewModelFromUserDetails(userDetails);
        return new AuthenticationResponse(jwt, accountInfo);
    }
}
