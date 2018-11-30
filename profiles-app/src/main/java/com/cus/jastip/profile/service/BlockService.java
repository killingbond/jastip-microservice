package com.cus.jastip.profile.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.BlockedByList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.repository.BlockListRepository;
import com.cus.jastip.profile.repository.BlockedByListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.BlockListSearchRepository;
import com.cus.jastip.profile.repository.search.BlockedByListSearchRepository;

@Service
public class BlockService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private BlockListRepository blockListRepository;

	@Autowired
	private BlockListSearchRepository blockListSearchRepository;

	@Autowired
	private BlockedByListRepository blockedByListRepository;

	@Autowired
	private BlockedByListSearchRepository blockedByListSearchRepository;

	/*
	 * Author : aditya P Rulian, funggsional :Bloocking someonet , tanggal : 30-11-2018
	 */
	public void updateBlockList(BlockList blockList) {
		if (blockList.getProfile().getId() != blockList.getBlockedProfileId()) {
			BlockList block = blockListRepository.findByblockedProfileIdInAndProfileIn(blockList.getBlockedProfileId(),
					blockList.getProfile());
			if (block == null) {
				blockList.setBlock(true);
				blockList.setBlockDate(Instant.now());
				BlockList result = blockListRepository.save(blockList);
				blockListSearchRepository.save(result);
				blockedByList(blockList.getProfile().getId(), blockList.getBlockedProfileId());
			} else if (block != null) {
				block.setBlock(blockList.isBlock());
				block.setBlockDate(Instant.now());
				BlockList result = blockListRepository.save(blockList);
				blockListSearchRepository.save(result);
				blockedByListStatus(blockList.getProfile().getId(), blockList.getBlockedProfileId(),
						blockList.isBlock());
			}
		}
	}

	public void blockedByListStatus(Long blokerProfileId, Long profileId, Boolean status) {
		Profile profile = profileRepository.findOne(profileId);
		BlockedByList block = blockedByListRepository.findByblokerProfileIdInAndProfileIn(blokerProfileId, profile);
		block.setBlockDate(Instant.now());
		block.setBlocked(status);
		BlockedByList result = blockedByListRepository.save(block);
		blockedByListSearchRepository.save(result);
	}

	public void blockedByList(Long blokerProfileId, Long profileId) {
		Profile profile = profileRepository.findOne(profileId);
		BlockedByList b = new BlockedByList();
		b.setBlockDate(Instant.now());
		b.setBlokerProfileId(blokerProfileId);
		b.setProfile(profile);
		b.setBlocked(true);
		BlockedByList result = blockedByListRepository.save(b);
		blockedByListSearchRepository.save(result);
	}

}
