package com.badminton.club.dto;

import java.util.ArrayList;
import java.util.List;
/**
 * 我要報名
 * 報名資料(個人加親友)
 */
public class ApplyAllDTO {
	//報名資料(個人)
	private ApplyDTO applyDTO;
	//報名人員清單列表(親友資料)
	private List<FriendDTO> friendDTOs=new ArrayList<>();

	public ApplyDTO getApplyDTO() {
		return applyDTO;
	}

	public void setApplyDTO(ApplyDTO applyDTO) {
		this.applyDTO = applyDTO;
	}

	public List<FriendDTO> getFriendDTOs() {
		return friendDTOs;
	}

	public void setFriendDTOs(List<FriendDTO> friendDTOs) {
		this.friendDTOs = friendDTOs;
	}
	
	
}
