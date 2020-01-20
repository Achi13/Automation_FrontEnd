package com.automation.db.campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.DependentRecordExecution;

public interface DependentRecordExecutionRepository extends JpaRepository<DependentRecordExecution, Long> {

}
