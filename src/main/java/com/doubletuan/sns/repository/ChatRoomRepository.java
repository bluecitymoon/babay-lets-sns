package com.doubletuan.sns.repository;

import com.doubletuan.sns.domain.ChatRoom;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChatRoom entity.
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

}
