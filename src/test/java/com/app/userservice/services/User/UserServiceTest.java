package com.app.userservice.services.User;

import com.app.userservice.exceptions.general.DuplicateException;
import com.app.userservice.exceptions.general.EntityNotFoundException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.User;
import com.app.userservice.repos.UserRepository;
import com.app.userservice.services.UserService;
import com.app.userservice.services.cache.interfaces.UserCacheService;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import com.app.userservice.services.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserCacheService userCacheService;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldSaveAndCache() {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setEmail("john@example.com");
        createDto.setName("John");
        createDto.setSurname("Doe");
        createDto.setBirthDate(LocalDate.of(2000, 1, 1));

        User user = new User();
        user.setEmail(createDto.getEmail());

        UserDto expectedDto = new UserDto();
        expectedDto.setEmail(createDto.getEmail());

        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        UserDto actual = userService.createUser(createDto);

        assertEquals(expectedDto.getEmail(), actual.getEmail());
        verify(userCacheService).putInCache(user);
    }

    @Test
    void createUser_shouldThrowDuplicateException() {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setEmail("john@example.com");

        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> userService.createUser(createDto));
    }

    @Test
    void findById_shouldReturnUserFromCache() {
        User user = new User();
        user.setId(1);

        when(userCacheService.getFromCache(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto dto = userService.findById(1);

        verify(userCacheService, never()).putInCache(any());
        assertNotNull(dto);
    }

    @Test
    void findById_shouldFetchFromDbIfCacheEmpty() {
        User user = new User();
        user.setId(2);

        when(userCacheService.getFromCache(2)).thenReturn(Optional.empty());
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto dto = userService.findById(2);

        verify(userCacheService).putInCache(user);
        assertNotNull(dto);
    }

    @Test
    void findUsersByIds_shouldThrowIfEmptyIds() {
        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByIds(List.of()));
    }

    @Test
    void findUsersByIds_shouldReturnFromCacheAndDb() {
        User cachedUser = new User();
        cachedUser.setId(1);
        User dbUser = new User();
        dbUser.setId(2);

        when(userCacheService.getFromCache(1)).thenReturn(Optional.of(cachedUser));
        when(userCacheService.getFromCache(2)).thenReturn(Optional.empty());
        when(userRepository.findAllById(List.of(2))).thenReturn(List.of(dbUser));
        when(userMapper.toDto(any())).thenReturn(new UserDto());

        List<UserDto> result = userService.findUsersByIds(List.of(1, 2));

        verify(userCacheService).putInCache(dbUser);
        assertEquals(2, result.size());
    }

    @Test
    void findByEmail_shouldReturnFromCache() {
        User user = new User();
        user.setEmail("email@example.com");

        when(userCacheService.getByEmailFromCache("email@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto dto = userService.findByEmail("email@example.com");

        verify(userCacheService, never()).putInCache(any());
        assertNotNull(dto);
    }

    @Test
    void findByEmail_shouldFetchFromDbIfNotInCache() {
        User user = new User();
        user.setEmail("email@example.com");

        when(userCacheService.getByEmailFromCache("email@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        UserDto dto = userService.findByEmail("email@example.com");

        verify(userCacheService).putInCache(user);
        assertNotNull(dto);
    }

    @Test
    void updateUser_shouldUpdateAndCache() {
        UserDto updatedDto = new UserDto();
        updatedDto.setEmail("updated@example.com");

        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateEntityFromDto(updatedDto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(updatedDto);

        UserDto result = userService.updateUser(1, updatedDto);

        verify(userCacheService).putInCache(user);
        assertEquals(updatedDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_shouldThrowIfNotFound() {
        UserDto updatedDto = new UserDto();
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1, updatedDto));
    }

    @Test
    void deleteUserById_shouldDeleteAndEvictCache() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUserById(1);

        verify(userRepository).deleteById(1);
        verify(userCacheService).deleteFromCache(1);
    }

    @Test
    void deleteUserById_shouldThrowIfNotExists() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1));
    }

    @Test
    void existsById_shouldReturnTrueFromCache() {
        when(userCacheService.getFromCache(1)).thenReturn(Optional.of(new User()));

        assertTrue(userService.existsById(1));
        verify(userRepository, never()).existsById(1);
    }

    @Test
    void existsById_shouldReturnTrueFromDbIfNotInCache() {
        when(userCacheService.getFromCache(1)).thenReturn(Optional.empty());
        when(userRepository.existsById(1)).thenReturn(true);

        assertTrue(userService.existsById(1));
    }

    @Test
    void existsById_shouldReturnFalseIfNotFound() {
        when(userCacheService.getFromCache(1)).thenReturn(Optional.empty());
        when(userRepository.existsById(1)).thenReturn(false);

        assertFalse(userService.existsById(1));
    }

    @Test
    void findUserById_shouldReturnFromCache() {
        User user = new User();
        when(userCacheService.getFromCache(1)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1);

        assertEquals(user, result);
    }

    @Test
    void findUserById_shouldFetchFromDbIfNotInCache() {
        User user = new User();

        when(userCacheService.getFromCache(2)).thenReturn(Optional.empty());
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        User result = userService.findUserById(2);

        verify(userCacheService).putInCache(user);
        assertEquals(user, result);
    }

    @Test
    void findUserById_shouldThrowIfNotFound() {
        when(userCacheService.getFromCache(3)).thenReturn(Optional.empty());
        when(userRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(3));
    }
}


