package com.app.userservice.services;

import com.app.userservice.models.User;
import com.app.userservice.repos.UserRepository;
import com.app.userservice.services.dto.UserDto;
import com.app.userservice.services.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public CompletableFuture<UserDto> createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return CompletableFuture.completedFuture(userMapper.toDto(user));
    }

    public CompletableFuture<UserDto> findById(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with ID " + id + " not found"));

        return CompletableFuture.completedFuture(userMapper.toDto(user));
    }

    public CompletableFuture<List<UserDto>> findUsersByIds(List<Integer> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<UserDto> dtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    public CompletableFuture<UserDto> findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email + " not found"));
        return CompletableFuture.completedFuture(userMapper.toDto(user));
    }

    @Transactional
    public CompletableFuture<UserDto> updateUser(int id, UserDto updatedDto) {
        User user = userRepository.findById(id).orElseThrow();

        user.setName(updatedDto.getName());
        user.setSurname(updatedDto.getSurname());
        user.setBirthday(updatedDto.getBirthDate());
        user.setEmail(updatedDto.getEmail());

        user = userRepository.save(user);
        return CompletableFuture.completedFuture(userMapper.toDto(user));
    }

    @Transactional
    public CompletableFuture<Void> deleteUserById(int id) {
        userRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }
}
