package com.cus.jastip.transaction.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.PostingStatus;
import com.cus.jastip.transaction.repository.PostingRepository;
import com.cus.jastip.transaction.web.rest.PostingResource;

@Service
public class PostingThreadService {

	@Autowired
	private PostingRepository postingRepository;

	@Autowired
	private PostingResource postingResource;

	Instant dateNow = Instant.now();

	/*
	 * Author : aditya P Rulian, funggsional : Thread offering when date is expired,
	 * tanggal : 30-11-2018
	 */
	public void postingExpired() throws URISyntaxException {
		List<Posting> posting = postingRepository.getPostingByStatusOpen();
		if (posting.size() != 0) {
			for (Posting post : posting) {
				ZoneId zone = ZoneId.of(post.getTimezone());
				ZonedDateTime zoneNow = ZonedDateTime.ofInstant(dateNow, zone);
				ZonedDateTime zoneExpired = ZonedDateTime.ofInstant(post.getExpiredDate(), zone);
				int compare = zoneExpired.compareTo(zoneNow);
				if (compare < 0) {
					post.setStatus(PostingStatus.EXPIRED);
					postingResource.updatePosting(post);
				}
			}
		}
	}

}
