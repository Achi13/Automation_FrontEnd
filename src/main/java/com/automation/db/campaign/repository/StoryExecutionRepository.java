package com.automation.db.campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.StoryExecution;

public interface StoryExecutionRepository extends JpaRepository<StoryExecution, Long>{

}
