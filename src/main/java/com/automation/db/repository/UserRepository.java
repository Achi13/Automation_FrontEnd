package com.automation.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findUserByUserId(int id);
	User findUserByUsername(String username);
	
}
