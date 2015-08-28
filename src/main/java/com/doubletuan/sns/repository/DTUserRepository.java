package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.DTUser;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DTUser entity.
 */
public interface DTUserRepository extends JpaRepository<DTUser,Long> {

}
