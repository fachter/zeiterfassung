package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.useCases.Authenticate;
import com.fhws.zeiterfassung.boundaries.useCases.CreateNewUser;
import com.fhws.zeiterfassung.exceptions.EmailAlreadyExistsException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserAlreadyExistsException;
import com.fhws.zeiterfassung.viewModels.AuthenticationRequest;
import com.fhws.zeiterfassung.viewModels.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final CreateNewUser createNewUser;
    private final Authenticate authenticate;
//    private final AuthenticationManager authenticationManager;
//    private final UserRepositoryUserDetailsService userDetailsService;
//    private final JwtUtil jwtTokenUtil;
//    private final GetAccountInformation getAccountInformation;

    public AuthenticationController(CreateNewUser createNewUser, Authenticate authenticate) {
        this.createNewUser = createNewUser;
        this.authenticate = authenticate;
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
            return new ResponseEntity<>(authenticate.authenticate(
                    new AuthenticationRequest(registerRequest.getUsername(), registerRequest.getPassword())),
                    HttpStatus.ACCEPTED);
        } catch (UserAlreadyExistsException | EmailAlreadyExistsException e) {
            return new ResponseEntity<>(registerRequest, HttpStatus.BAD_REQUEST);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>("Invalid data", HttpStatus.valueOf(406));
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong with the login", HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticate.authenticate(authenticationRequest));
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
