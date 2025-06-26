package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.controller.AuthController;
import com.example.foodwasteapp.dto.*;
import com.example.foodwasteapp.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
//alat za mockanje
import org.mockito.*;
import org.springframework.http.ResponseEntity;

//import zbog statičkih metoda koje se koriste
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Sa jUnit testovima testiramo dio po dio (npr.metodu po metodu). Mockanjem se testu daju ulazni podaci
//i od njega se traži da vrati nešto hardkodirano (odnosno, vrijednost koja je doslovno ručno
//upisana u kod umjesto da se dohvaća iz neke konfiguracije, baze, korisničkog unosa...

 //definiranje klase
class AuthControllerUnitTest {

    //uz pomoć mockita stvaramo mock objekt authService koji simulira stvarni AuthService jer su
     //jUnit testovi takvi da ne trebaju koristiti service i izvlačiti podatke iz prave baze,
     //nego se svi ti podaci mockaju
    @Mock
    private AuthService authService;


    //mockito injecta mockani authService u AuthController
    @InjectMocks
    private AuthController controller;


    //metoda koja se pokreće prije svakog testa
     //openMocks aktivira anotacije iz klase
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    //Test za provjeru ispravnog ponašanja login() metode u controlleru
    @Test
    void login_shouldReturnResponseFromService() {

        //Kreira se DTO objekt sa login podacima koji će biti predan controlleru
        LoginRequestDto req = new LoginRequestDto();
        req.setUsername("admin");
        req.setPassword("admin");


        //očekivani odgovor koji bi authService.login() trebao vratiti
        AuthResponseDto respDto = AuthResponseDto.builder()
                .accessToken("a")
                .refreshToken("r")
                .role("USER")
                .message("msg")
                .build();

        //definiranje sto ce mockani authService.login vratiti kada dobije očekivane argumente
        when(authService.login(eq(req), any(HttpServletResponse.class)))
                .thenReturn(ResponseEntity.ok(respDto));

        //poziva se metoda login() u AuthController i rezultat se sprema
        ResponseEntity<AuthResponseDto> resp = controller.login(req, mock(HttpServletResponse.class));

        //provjera da je status code 200 (ok)
        assertEquals(200, resp.getStatusCodeValue());
        //provjera da se accesToken poklapa s ocekivanim
        assertEquals("a", resp.getBody().getAccessToken());
        //provjera da je authService pozvan jednom s točnim argumentima
        verify(authService).login(eq(req), any(HttpServletResponse.class));
    }

    @Test
    void refresh_shouldReturnResponseFromService() {
        RefreshTokenRequestDto req = new RefreshTokenRequestDto();
        req.setRefreshToken("r");

        AuthResponseDto out = AuthResponseDto.builder()
                .accessToken("newA")
                .refreshToken("r")
                .message("m")
                .build();

        when(authService.refreshToken(anyString(), any(HttpServletResponse.class)))
                .thenReturn(ResponseEntity.ok(out));

        ResponseEntity<AuthResponseDto> resp = controller.refresh(req, mock(HttpServletResponse.class));

        assertEquals("r", resp.getBody().getRefreshToken());

        verify(authService).refreshToken(eq("r"), any(HttpServletResponse.class));
    }


    @Test
    void logout_shouldCallServiceAndReturnOk() {
        RefreshTokenRequestDto req = new RefreshTokenRequestDto();
        req.setRefreshToken("r");

        ResponseEntity<Void> resp = controller.logout(req);

        assertEquals(200, resp.getStatusCodeValue());
        verify(authService).logout("r");
    }

    @Test
    void register_shouldReturnServiceResponse() {
        RegistrationRequestDto req = new RegistrationRequestDto();
        req.setUsername("u"); req.setEmail("e@e.com"); req.setPassword("p");

        ResponseEntity<?> out = ResponseEntity.status(201).body(AuthResponseDto.builder().build());

        when(authService.register(req)).thenReturn((ResponseEntity<AuthResponseDto>) out);

        ResponseEntity<?> resp = controller.register(req);

        assertEquals(201, resp.getStatusCodeValue());
        verify(authService).register(req);
    }
}
