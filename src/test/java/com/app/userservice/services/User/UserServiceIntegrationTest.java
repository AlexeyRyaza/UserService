package com.app.userservice.services.User;

import com.app.userservice.services.UserService;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private UserService userService;

    @Test
    void createUser_and_findById_shouldWorkCorrectly() {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setEmail("john@example.com");

        UserDto created = userService.createUser(dto);
        UserDto fetched = userService.findById(created.getId());

        assertEquals("john@example.com", fetched.getEmail());
    }

    @Test
    void findByEmail_shouldReturnCorrectUser() {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("Alice");
        dto.setSurname("Smith");
        dto.setBirthDate(LocalDate.of(1995, 5, 15));
        dto.setEmail("alice@example.com");

        userService.createUser(dto);

        UserDto found = userService.findByEmail("alice@example.com");
        assertEquals("Alice", found.getName());
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setName("Bob");
        createDto.setSurname("Brown");
        createDto.setBirthDate(LocalDate.of(1988, 3, 10));
        createDto.setEmail("bob@example.com");

        UserDto created = userService.createUser(createDto);

        UserDto updateDto = new UserDto();
        updateDto.setId(created.getId());
        updateDto.setName("Bobby");
        updateDto.setSurname("Brown");
        updateDto.setBirthDate(LocalDate.of(1988, 3, 10));
        updateDto.setEmail("bob@example.com");

        UserDto updated = userService.updateUser(created.getId(), updateDto);

        assertEquals("Bobby", updated.getName());
    }

    @Test
    void deleteUserById_shouldRemoveUser() {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("Charlie");
        dto.setSurname("Davis");
        dto.setBirthDate(LocalDate.of(1992, 7, 21));
        dto.setEmail("charlie@example.com");

        UserDto created = userService.createUser(dto);

        userService.deleteUserById(created.getId());

        org.junit.jupiter.api.Assertions.assertThrows(
                com.app.userservice.exceptions.general.EntityNotFoundException.class,
                () -> userService.findById(created.getId())
        );
    }

    @Test
    void existsById_shouldReturnTrueIfUserExists() {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("Diana");
        dto.setSurname("Evans");
        dto.setBirthDate(LocalDate.of(1999, 12, 12));
        dto.setEmail("diana@example.com");

        UserDto created = userService.createUser(dto);

        boolean exists = userService.existsById(created.getId());

        assertTrue(exists);
    }

    @Test
    void findUsersByIds_shouldReturnAllUsers() {
        UserCreateDto dto1 = new UserCreateDto();
        dto1.setName("Eve");
        dto1.setSurname("Ford");
        dto1.setBirthDate(LocalDate.of(1985, 4, 5));
        dto1.setEmail("eve@example.com");

        UserCreateDto dto2 = new UserCreateDto();
        dto2.setName("Frank");
        dto2.setSurname("Green");
        dto2.setBirthDate(LocalDate.of(1987, 6, 6));
        dto2.setEmail("frank@example.com");

        UserDto user1 = userService.createUser(dto1);
        UserDto user2 = userService.createUser(dto2);

        var users = userService.findUsersByIds(List.of(user1.getId(), user2.getId()));

        assertEquals(2, users.size());
        var emails = users.stream().map(UserDto::getEmail).toList();
        org.junit.jupiter.api.Assertions.assertTrue(emails.contains("eve@example.com"));
        org.junit.jupiter.api.Assertions.assertTrue(emails.contains("frank@example.com"));
    }
}

