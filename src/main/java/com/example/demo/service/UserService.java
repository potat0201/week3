package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private Long nextId  = 1L;

    public User createUser(UserRequest request){
        User user = new User();

        user.setId(nextId++);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        users.add(user);
        return user;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserById(Long id){
        for(User u : users){
            if(id.equals(u.getId())){
                return u;
            }
        }
        throw new UserNotFoundException("User không tồn tại");
    }

    public User updateUserById(Long id, UserRequest request){
        for(User u : users){
            if(id.equals(u.getId())){
                u.setName(request.getName());
                u.setEmail(request.getEmail());
                u.setAge(request.getAge());
                return u;
            }
        }
        throw new UserNotFoundException("User không tồn tại");
    }

    public boolean deleteUserById(Long id){

        for(User u : users){
            if(id.equals(u.getId())){
                users.remove(u);
                return true;
            }
        }

        throw new UserNotFoundException("User không tồn tại");
    }
}
