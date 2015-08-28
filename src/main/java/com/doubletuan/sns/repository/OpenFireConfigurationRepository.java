package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.OpenFireConfiguration;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OpenFireConfiguration entity.
 */
public interface OpenFireConfigurationRepository extends JpaRepository<OpenFireConfiguration,Long> {
	
	OpenFireConfiguration findByIdentifier(String identifier);

}
