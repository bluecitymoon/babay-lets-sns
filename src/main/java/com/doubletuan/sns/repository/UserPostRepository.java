package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.UserPost;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPost entity.
 */
public interface UserPostRepository extends JpaRepository<UserPost,Long> {

}
