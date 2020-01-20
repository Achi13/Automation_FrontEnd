package com.automation.db.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.entity.Record;
import com.automation.db.entity.User;

public interface RecordRepository extends JpaRepository<Record, Long> {
	
	Record findRecordByUser(User user);
	Record findRecordByTestcase(String testcase);
	List<Record> findAllRecordsByUser(User user);
	
}
