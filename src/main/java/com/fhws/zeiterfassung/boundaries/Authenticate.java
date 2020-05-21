package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.AuthenticationRequest;
import com.fhws.zeiterfassung.models.AuthenticationResponse;

public interface Authenticate {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception;
}
