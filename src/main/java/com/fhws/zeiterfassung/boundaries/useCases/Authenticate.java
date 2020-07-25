package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.viewModels.AuthenticationRequest;
import com.fhws.zeiterfassung.viewModels.AuthenticationResponse;

public interface Authenticate {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception;
}
