package com.automation.db.campaign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.Campaign;
import com.automation.db.entity.User;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	
	Campaign findCampaignByCampaignId(int id);
	List<Campaign> findAllCampaignByUserId(User user);
	
}
