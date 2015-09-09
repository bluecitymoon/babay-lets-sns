package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.PostImage;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PostImage entity.
 */
public interface PostImageRepository extends JpaRepository<PostImage,Long> {

	@Query("select src from PostImage where userpost_id = ?1")
	public List<String> findSrcByPostId(Long postId);
}
