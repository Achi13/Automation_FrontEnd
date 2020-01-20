package com.automation.db.campaign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.Campaign;
import com.automation.db.campaign.CampaignExecution;
import com.automation.db.entity.User;

public interface CampaignExecutionRepository extends JpaRepository<CampaignExecution, Long>{
	
	//Campaign findCampaignByCampaignId(int id);
	//List<Campaign> findAllCampaignByUserId(User user);
	
	List<CampaignExecution> findAllCampaignExecutionByCampaignId(Campaign campaignId);
	
	
	
}
