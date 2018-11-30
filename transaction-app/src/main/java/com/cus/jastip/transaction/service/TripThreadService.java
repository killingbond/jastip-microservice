package com.cus.jastip.transaction.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.TripStatus;
import com.cus.jastip.transaction.repository.TripRepository;
import com.cus.jastip.transaction.web.rest.TripResource;

@Service
public class TripThreadService {

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private TripResource tripResource;

	Instant dateNow = Instant.now();

	/*
	 * Author : aditya P Rulian, funggsional : Thread offering when date is Pased,
	 * tanggal : 30-11-2018
	 */
	public void tripExpired() throws URISyntaxException {
		List<Trip> trip = tripRepository.getPostingOngoingOrUpcoming();
		if (trip.size() != 0) {
			for (Trip tr : trip) {
				ZoneId zone = ZoneId.of(tr.getTimezone());
				ZonedDateTime zoneNow = ZonedDateTime.ofInstant(dateNow, zone);
				ZonedDateTime zoneExpired = ZonedDateTime.ofInstant(tr.getEndDate(), zone);
				int compare = zoneExpired.compareTo(zoneNow);
				if (compare < 0) {
					tr.setStatus(TripStatus.PAST);
					Trip result = tripRepository.save(tr);
					tripResource.updateTrip(result);
				}

			}
		}
	}

}
