package com.doubletuan.sns.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doubletuan.sns.domain.UserPost;

/**
 * Spring Data JPA repository for the UserPost entity.
 */
public interface UserPostRepository extends JpaRepository<UserPost,Long> {
	
	public Page<UserPost> findByJidInOrderByIdDesc(Collection<String> jids, Pageable pageable);
	

}
