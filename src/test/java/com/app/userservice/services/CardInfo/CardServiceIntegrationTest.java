package com.app.userservice.services.CardInfo;

import com.app.userservice.exceptions.card.CardNotFoundException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.User;
import com.app.userservice.repos.CardRepository;
import com.app.userservice.repos.UserRepository;
import com.app.userservice.services.CardService;
import com.app.userservice.services.UserService;
import com.app.userservice.services.cache.interfaces.CardCacheService;
import com.app.userservice.services.dto.cardInfo.CardInfoCreateDto;
import com.app.userservice.services.dto.cardInfo.CardInfoDto;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import com.app.userservice.services.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CardServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CardCacheService cardCacheService;

    private User testUser;

    @BeforeEach
    void setup() {
        cardRepository.deleteAll();
        userRepository.deleteAll();

        UserDto dto = userService.createUser(new UserCreateDto(
                "Test",
                "User",
                LocalDate.of(1990, 1, 1),
                "testuser@example.com"
        ));

        testUser = userMapper.toEntity(dto);
    }

    @Test
    void createCard_and_findById_shouldWorkCorrectly() {
        CardInfoCreateDto createDto = new CardInfoCreateDto();
        createDto.setNumber("1234567890123456");
        createDto.setHolder("Test User");
        createDto.setExpirationDate(LocalDate.of(2030, 12, 31));
        createDto.setUserId(testUser.getId());

        CardInfoDto created = cardService.createCard(createDto);

        assertNotNull(created.getId());
        assertEquals("1234567890123456", created.getNumber());
        assertEquals("Test User", created.getHolder());
        assertEquals(testUser.getId(), created.getUserId());

        CardInfoDto fetched = cardService.findById(created.getId());
        assertEquals(created.getId(), fetched.getId());
        assertEquals(created.getNumber(), fetched.getNumber());
    }

    @Test
    void findCardsByIds_shouldReturnAllCards() {
        CardInfoCreateDto c1 = new CardInfoCreateDto("1111222233334444", "Holder One", testUser.getId(), LocalDate.of(2025, 6, 30));
        CardInfoCreateDto c2 = new CardInfoCreateDto("5555666677778888", "Holder Two", testUser.getId(), LocalDate.of(2026, 7, 31));
        CardInfoDto card1 = cardService.createCard(c1);
        CardInfoDto card2 = cardService.createCard(c2);

        List<CardInfoDto> cards = cardService.findCardsByIds(List.of(card1.getId(), card2.getId()));

        assertEquals(2, cards.size());
        assertTrue(cards.stream().anyMatch(c -> c.getNumber().equals("1111222233334444")));
        assertTrue(cards.stream().anyMatch(c -> c.getNumber().equals("5555666677778888")));
    }

    @Test
    void updateCard_shouldUpdateAndReturnUpdated() {
        CardInfoCreateDto createDto = new CardInfoCreateDto("9999888877776666", "Old Holder", testUser.getId(), LocalDate.of(2028, 11, 30));
        CardInfoDto created = cardService.createCard(createDto);

        CardInfoDto updateDto = new CardInfoDto(created.getId(), "9999888877776666", "New Holder", testUser.getId(), LocalDate.of(2029, 12, 31));
        CardInfoDto updated = cardService.updateCard(created.getId(), updateDto);

        assertEquals("New Holder", updated.getHolder());

        CardInfoDto fetched = cardService.findById(created.getId());
        assertEquals("New Holder", fetched.getHolder());
    }

    @Test
    void deleteCardById_shouldDeleteCard() {
        CardInfoCreateDto createDto = new CardInfoCreateDto("2222333344445555", "Holder To Delete", testUser.getId(), LocalDate.of(2030, 1, 1));
        CardInfoDto created = cardService.createCard(createDto);

        cardService.deleteCardById(created.getId());

        assertThrows(CardNotFoundException.class, () -> cardService.findById(created.getId()));
    }

    @Test
    void createCard_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        CardInfoCreateDto createDto = new CardInfoCreateDto("0000111122223333", "No User Holder", 999, LocalDate.of(2030, 1, 1));

        assertThrows(UserNotFoundException.class, () -> cardService.createCard(createDto));
    }

    @Test
    void findById_shouldThrowCardNotFoundException_whenCardDoesNotExist() {
        assertThrows(CardNotFoundException.class, () -> cardService.findById(99999));
    }

    @Test
    void updateCard_shouldThrowCardNotFoundException_whenCardDoesNotExist() {
        CardInfoDto dto = new CardInfoDto(99999, "0000111122223333", "Holder", testUser.getId(), LocalDate.of(2030, 1, 1));
        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(99999, dto));
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        CardInfoCreateDto createDto = new CardInfoCreateDto("3333444455556666", "Exist Holder", testUser.getId(), LocalDate.of(2031, 1, 1));
        CardInfoDto created = cardService.createCard(createDto);

        assertTrue(cardService.existsById(created.getId()));
    }

    @Test
    void existsById_shouldReturnFalseIfNotExists() {
        assertFalse(cardService.existsById(99999));
    }
}
