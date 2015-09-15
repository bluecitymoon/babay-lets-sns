package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.CommentReply;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CommentReply entity.
 */
public interface CommentReplyRepository extends JpaRepository<CommentReply,Long> {

}
