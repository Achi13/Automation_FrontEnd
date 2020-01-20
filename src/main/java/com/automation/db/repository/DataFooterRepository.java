package com.automation.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.entity.DataFooter;
import com.automation.db.entity.Record;

public interface DataFooterRepository extends JpaRepository<DataFooter, Long> {

	DataFooter findDataFooterByTestcaseNumber(Record testcaseNumber);
	
}
