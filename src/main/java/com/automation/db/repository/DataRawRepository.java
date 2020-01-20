package com.automation.db.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automation.db.entity.DataRaw;
import com.automation.db.entity.Record;

public interface DataRawRepository extends JpaRepository<DataRaw, Long> {
	
	List<DataRaw> findAllDataRawByTestcaseNumber(Record testcaseNumber);
	@Transactional
	@Modifying
	@Query("delete from DataRaw d where d.testcaseNumber = :testcase")
	void removeDataRawsByTestcaseNumber(@Param("testcase")Record testcase);
	
}
