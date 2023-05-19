package com.example.memorip.repository;

import com.example.memorip.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<Users, Long> {
    @Override
    ArrayList<Users> findAll();
}
