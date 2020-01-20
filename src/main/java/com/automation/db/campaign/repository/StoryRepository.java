package com.automation.db.campaign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.db.campaign.Story;
import com.automation.db.campaign.Theme;

public interface StoryRepository extends JpaRepository<Story, Long> {
	
	Story findStoryByStoryId(int id);
	List<Story> findAllStoryByThemeId(Theme themeId);

}
