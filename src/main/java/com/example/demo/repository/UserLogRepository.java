package com.example.demo.repository;


import com.example.demo.entity.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends MongoRepository<UserLog, String> {
}
