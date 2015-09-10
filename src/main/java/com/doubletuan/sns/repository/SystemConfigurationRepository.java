package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.SystemConfiguration;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SystemConfiguration entity.
 */
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration,Long> {

	@Query("select currentValue from SystemConfiguration where identity = ?1")
	public String findConfigurationByIdentity(String identity);
}
