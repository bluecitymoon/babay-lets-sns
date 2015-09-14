package com.doubletuan.sns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doubletuan.sns.domain.PostComment;

/**
 * Spring Data JPA repository for the PostComment entity.
 */
public interface PostCommentRepository extends JpaRepository<PostComment,Long> {

	List<PostComment> findByUserPost_Id(Long userPostId);

}
