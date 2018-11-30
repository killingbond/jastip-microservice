package com.cus.jastip.transaction.service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Offering;
import com.cus.jastip.transaction.domain.enumeration.OfferingStatus;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingRepository;
import com.cus.jastip.transaction.web.rest.OfferingResource;

@Service
public class OfferingThreadService {

	@Autowired
	private OfferingRepository offeringRepository;

	@Autowired
	private OfferingResource offeringResource;

	@Autowired
	private TransactionAuditService transactionAuditService;

	private static final String ENTITY_NAME = "offering";

	Instant dateNow = Instant.now();

	/*
	 * Author : aditya P Rulian, funggsional : Thread offering when date is expired,
	 * tanggal : 30-11-2018
	 */
	@SuppressWarnings("static-access")
	public void offeringExpired() throws URISyntaxException {
		List<Offering> offering = offeringRepository.getStatusNew();
		if (offering.size() != 0) {
			for (Offering off : offering) {

				ZoneId zone = ZoneId.of(off.getTimezone());
				/*
				 * if (zone.getAvailableZoneIds() == null) { zone = ZoneId.of("Asia/Jakarta"); }
				 */
				ZonedDateTime zoneNow = ZonedDateTime.ofInstant(dateNow, zone);
				ZonedDateTime zoneExpired = ZonedDateTime.ofInstant(off.getOfferingExpiredDate(), zone);
				int compare = zoneExpired.compareTo(zoneNow);
				if (compare < 0) {
					off.setStatus(OfferingStatus.EXPIRED);
					offeringResource.updateOffering(off);
				}

			}
		}
	}

}
