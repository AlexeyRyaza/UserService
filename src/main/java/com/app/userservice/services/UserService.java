package com.app.userservice.services;

import com.app.userservice.exceptions.general.DuplicateException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.User;
import com.app.userservice.repos.UserRepository;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import com.app.userservice.services.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public UserDto createUser(UserCreateDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new DuplicateException("User with email: " + userDto.getEmail() + " already exists");
        }

        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto findById(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with ID " + id + " not found"));

        return userMapper.toDto(user);
    }

    public List<UserDto> findUsersByIds(List<Integer> ids) {
        if(ids.isEmpty()){
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email + " not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(int id, UserDto updatedDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateEntityFromDto(updatedDto, existingUser);

        return userMapper.toDto(existingUser);
    }

    @Transactional
    public void deleteUserById(int id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }
}
