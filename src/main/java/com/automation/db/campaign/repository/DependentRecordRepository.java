package com.automation.db.campaign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.DependentRecord;
import com.automation.db.campaign.Story;

public interface DependentRecordRepository extends JpaRepository<DependentRecord, Long> {
	
	DependentRecord findDependentRecordByTestcaseNumber(String testcase);
	List<DependentRecord> findAllDependentRecordByStoryId(Story storyId);

}
