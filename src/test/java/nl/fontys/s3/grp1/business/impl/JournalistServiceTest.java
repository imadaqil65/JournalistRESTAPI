package nl.fontys.s3.grp1.business.impl;

import nl.fontys.s3.grp1.business.exception.InvalidAccountException;
import nl.fontys.s3.grp1.business.exception.InvalidJournalistException;
import nl.fontys.s3.grp1.configuration.security.token.AccessToken;
import nl.fontys.s3.grp1.domain.Journalist;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistRequest;
import nl.fontys.s3.grp1.domain.dto.journalist.CreateJournalistResponse;
import nl.fontys.s3.grp1.domain.dto.journalist.UpdateJournalistRequest;
import nl.fontys.s3.grp1.persistence.AccountRepository;
import nl.fontys.s3.grp1.persistence.JournalistRepository;
import nl.fontys.s3.grp1.persistence.entity.AccountEntity;
import nl.fontys.s3.grp1.persistence.entity.JournalistEntity;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JournalistServiceTest {
    @Mock
    private JournalistRepository journalistRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private JournalistServiceImpl journalistServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateJournalist_Success() {
        CreateJournalistRequest request = new CreateJournalistRequest("email@example.com", "password", "First", "Last", LocalDate.of(2000,1,1));
        AccountEntity accountEntity = AccountEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        JournalistEntity journalistEntity = JournalistEntity.builder()
                .account(accountEntity)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .birthday(request.getBirthday())
                .build();

        when(accountRepository.findById(request.getEmail())).thenReturn(Optional.empty());
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(journalistRepository.save(any(JournalistEntity.class))).thenReturn(journalistEntity);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");


        CreateJournalistResponse response = journalistServiceImpl.createJournalist(request);

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
        verify(journalistRepository, times(1)).save(any(JournalistEntity.class));
    }

    @Test
    void testCreateJournalist_Failure_InvalidAccount() {
        CreateJournalistRequest request = new CreateJournalistRequest("email@example.com", "password", "First", "Last", LocalDate.of(2000,1,1));
        AccountEntity accountEntity = new AccountEntity();

        when(accountRepository.findById(request.getEmail())).thenReturn(Optional.of(accountEntity));

        assertThrows(InvalidAccountException.class, () -> journalistServiceImpl.createJournalist(request));
        verify(accountRepository, never()).save(any(AccountEntity.class));
        verify(journalistRepository, never()).save(any(JournalistEntity.class));
    }

    @Test
    void testUpdateJournalist_Success() {
        UpdateJournalistRequest request = new UpdateJournalistRequest(1L, "NewFirst", "NewLast", LocalDate.of(2000,1,1));
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setId(1L);

        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(journalistRepository.findById(request.getId())).thenReturn(Optional.of(journalistEntity));


        journalistServiceImpl.updateJournalist(request);

        verify(journalistRepository, times(1)).save(any(JournalistEntity.class));
    }

    @Test
    void testUpdateJournalist_Failure_InvalidJournalist() {
        UpdateJournalistRequest request = new UpdateJournalistRequest(1L, "NewFirst", "NewLast", LocalDate.of(2000,1,1));

        when(journalistRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(InvalidJournalistException.class, () -> journalistServiceImpl.updateJournalist(request));
        verify(journalistRepository, never()).save(any(JournalistEntity.class));
    }

    @Test
    void testDeleteJournalist_Success() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail("email@example.com");
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setAccount(accountEntity);

        when(journalistRepository.findById(anyLong())).thenReturn(Optional.of(journalistEntity));
        doNothing().when(journalistRepository).deleteById(anyLong());
        doNothing().when(accountRepository).deleteById(anyString());

        journalistServiceImpl.deleteJournalist(1L);

        verify(journalistRepository, times(1)).deleteById(anyLong());
        verify(accountRepository, times(1)).deleteById(anyString());
    }

    @Test
    void testGetJournalist_Success() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail("email@example.com");
        JournalistEntity journalistEntity = new JournalistEntity();
        journalistEntity.setAccount(accountEntity);

        when(journalistRepository.findById(anyLong())).thenReturn(Optional.of(journalistEntity));

        Optional<Journalist> response = journalistServiceImpl.getJournalist(1L);

        assertTrue(response.isPresent());
    }

    @Test
    void testGetJournalist_Failure() {
        when(journalistRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Journalist> response = journalistServiceImpl.getJournalist(1L);

        assertFalse(response.isPresent());

    }
}
