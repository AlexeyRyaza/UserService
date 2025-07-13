package com.app.userservice.services;

import com.app.userservice.exceptions.general.DuplicateException;
import com.app.userservice.exceptions.user.UserNotFoundException;
import com.app.userservice.models.User;
import com.app.userservice.repos.UserRepository;
import com.app.userservice.services.cache.interfaces.UserCacheService;
import com.app.userservice.services.dto.user.UserCreateDto;
import com.app.userservice.services.dto.user.UserDto;
import com.app.userservice.services.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCacheService userCacheService;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, UserCacheService userCacheService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCacheService = userCacheService;
    }

    @Transactional
    public UserDto createUser(UserCreateDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new DuplicateException("User with email: " + userDto.getEmail() + " already exists");
        }

        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        userCacheService.putInCache(user);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findById(int id) {
        return userMapper.toDto(userCacheService.getFromCache(id)
               .orElseGet(() -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    userCacheService.putInCache(user);
                    System.out.println("NO Cache");
                    return user;
        }));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findUsersByIds(List<Integer> ids) {
        if(ids.isEmpty()){
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        List<User> result = new ArrayList<>();
        List<Integer> idsToFetchFromDb = new ArrayList<>();

        for (Integer id : ids) {
            Optional<User> cachedUser = userCacheService.getFromCache(id);
            if (cachedUser.isPresent()) {
                result.add(cachedUser.get());
            } else {
                idsToFetchFromDb.add(id);
            }
        }

        if (!idsToFetchFromDb.isEmpty()) {
            List<User> dbUsers = userRepository.findAllById(idsToFetchFromDb);
            result.addAll(dbUsers);

            dbUsers.forEach(userCacheService::putInCache);
        }

        return result.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        Optional<User> cachedUser = userCacheService.getByEmailFromCache(email);
        if (cachedUser.isPresent()) {
            return userMapper.toDto(cachedUser.get());
        }

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email + " not found"));

        userCacheService.putInCache(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(int id, UserDto updatedDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateEntityFromDto(updatedDto, existingUser);
        User savedUser = userRepository.save(existingUser);
        userCacheService.putInCache(savedUser);

        return userMapper.toDto(existingUser);
    }

    @Transactional
    public void deleteUserById(int id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        userCacheService.deleteFromCache(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        Optional<User> cached = userCacheService.getFromCache(id);
        if (cached.isPresent()) {
            return true;
        }

        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public User findUserById(int id) {
        Optional<User> cached = userCacheService.getFromCache(id);
        if (cached.isPresent()) {
            return cached.get();
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userCacheService.putInCache(user);

        return user;
    }
}
