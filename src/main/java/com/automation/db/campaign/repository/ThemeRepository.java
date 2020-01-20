package com.automation.db.campaign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.Campaign;
import com.automation.db.campaign.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
	
	Theme findThemeByThemeId(int id);
	List<Theme> findAllThemeByCampaignId(Campaign campaignId);
	
}
