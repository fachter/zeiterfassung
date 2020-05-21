package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.CreateNewUser;
import com.fhws.zeiterfassung.boundaries.GetAccountInformation;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserAlreadyExistsException;
import com.fhws.zeiterfassung.models.AccountInformationViewModel;
import com.fhws.zeiterfassung.models.AuthenticationRequest;
import com.fhws.zeiterfassung.models.AuthenticationResponse;
import com.fhws.zeiterfassung.models.RegisterRequest;
import com.fhws.zeiterfassung.services.UserRepositoryUserDetailsService;
import com.fhws.zeiterfassung.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepositoryUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final CreateNewUser createNewUser;
    private final GetAccountInformation getAccountInformation;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepositoryUserDetailsService userDetailsService,
                                    JwtUtil jwtTokenUtil,
                                    CreateNewUser createNewUser,
                                    GetAccountInformation getAccountInformation) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.createNewUser = createNewUser;
        this.getAccountInformation = getAccountInformation;
    }

    //    @GetMapping("/testApiCall")
//    public ResponseEntity<String> testApiCall() {
//        String someString = "Hello from the backend";
//        return new ResponseEntity<>(someString, HttpStatus.OK);
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@RequestBody RegisterRequest registerRequest) {
        try {
            createNewUser.create(registerRequest);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>("Invalid data", HttpStatus.valueOf(406));
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        final AccountInformationViewModel accountInfo = getAccountInformation.getViewModelFromUserDetails(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, accountInfo));
    }
}
