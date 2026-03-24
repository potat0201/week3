package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserLog;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserLogRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserLogRepository userLogRepository;

    // CHỈ DÙNG 1 CONSTRUCTOR DUY NHẤT CHO CẢ 2 ÔNG THƯ KÝ
    public UserService(UserRepository userRepository, UserLogRepository userLogRepository) {
        this.userRepository = userRepository;
        this.userLogRepository = userLogRepository;
    }


    public User createUser(UserRequest request){
        User user = new User(request.getName(), request.getEmail(), request.getAge());
        User savedUser = userRepository.save(user);

        userLogRepository.save(new UserLog("CREATE", savedUser.getEmail()));
        return savedUser;
    }

    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id){
        System.out.println("Chạm đáy sql: đang tìm user id " + id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User không tồn tại"));
    }

    @CacheEvict(value = "users", key = "#id")
    public User updateUserById(Long id, UserRequest request){
        User existingUser = this.getUserById(id);

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setAge(request.getAge());

        User updateUser = userRepository.save(existingUser);

        userLogRepository.save(new UserLog("UPDATE", updateUser.getEmail()));

        System.out.println("Đã quét sạch cache cũ của user id " + id);

        return updateUser;
    }

    public boolean deleteUserById(Long id){
        User existingUser = this.getUserById(id);
        userRepository.delete(existingUser);
        userLogRepository.save(new UserLog("DELETE", existingUser.getEmail()));
        return true;
    }
}
