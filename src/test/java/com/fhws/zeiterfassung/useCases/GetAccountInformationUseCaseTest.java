package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.useCases.GetAccountInformation;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.viewModels.AccountInformationViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountInformationUseCaseTest {

    private GetAccountInformation useCase;
    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private UserGateway userGatewayMock;

    @BeforeEach
    void setUp() {
        useCase = new GetAccountInformationUseCase(userGatewayMock);
    }

    @Test
    public void givenNull() {
        AccountInformationViewModel viewModel = useCase.getViewModelFromUserDetails(null);

        assertThat(viewModel).usingRecursiveComparison().isEqualTo(new AccountInformationViewModel());
    }

    @Test
    public void givenNoUserExistsForGivenUserDetails() throws Exception {
        when(userDetailsMock.getUsername()).thenReturn("invalidUsername");
        when(userGatewayMock.getUserByUsername("invalidUsername")).thenThrow(UserDoesNotExistException.class);

        AccountInformationViewModel viewModel = useCase.getViewModelFromUserDetails(userDetailsMock);

        assertThat(viewModel).usingRecursiveComparison().isEqualTo(new AccountInformationViewModel());
    }

    @Test
    public void givenUserExistForGivenUserDetails() throws Exception {
        when(userDetailsMock.getUsername()).thenReturn("testUsername");
        User user = new User();
        user.setUsername("testUsername");
        user.setFullName("test User");
        user.setEmail("test@email");
        when(userGatewayMock.getUserByUsername("testUsername")).thenReturn(user);
        AccountInformationViewModel expectedViewModel = new AccountInformationViewModel()
                .setEmail("test@email")
                .setFullName("test User")
                .setUsername("testUsername");

        AccountInformationViewModel viewModel = useCase.getViewModelFromUserDetails(userDetailsMock);

        assertThat(viewModel).usingRecursiveComparison().isEqualTo(expectedViewModel);
    }
}