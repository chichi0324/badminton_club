package com.badminton.club.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.badminton.club.entity.AvtMessage;

public interface AvtMessageRepository extends JpaRepository<AvtMessage, Integer> {
	/**
	 * 取得活動的所有心得回饋
	 * 照留言時間排序(新-->舊)
	 */
	@Query(value = "select * from avt_message where avt_no=? order by msg_time desc", nativeQuery = true)
	List<AvtMessage> findAvtMessageByActivityDesc(int avtNo);
}
