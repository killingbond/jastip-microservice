package com.cus.jastip.banner.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.banner.domain.Banner;
import com.cus.jastip.banner.domain.BannerAudit;
import com.cus.jastip.banner.domain.BannerAuditConfig;
import com.cus.jastip.banner.domain.enumeration.PostingType;
import com.cus.jastip.banner.domain.enumeration.UpdateType;
import com.cus.jastip.banner.repository.BannerAuditConfigRepository;
import com.cus.jastip.banner.repository.BannerAuditRepository;
import com.cus.jastip.banner.repository.search.BannerAuditSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BannerAuditService {

	@Autowired
	private BannerAuditRepository bannerAuditRepository;

	@Autowired
	private BannerAuditSearchRepository bannerAuditSearchRepository;

	@Autowired
	private BannerAuditConfigRepository bannerAuditConfigRepository;

	Map<String, String> data = new HashMap<>();

	/*
	 * Author : aditya P Rulian, funggsional : audit banner , tanggal : 30-11-2018
	 */

	public void addBanner(Object object, String entityName, UpdateType eventType) {
		BannerAuditConfig config = bannerAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Banner result = (Banner) object;
			BannerAudit mAudit = new BannerAudit();
			mAudit.setEntityId(result.getId());
			mAudit.setEntityName(entityName);
			mAudit.setAuditEventType(eventType.toString());
			mAudit.setPrincipal(result.getLastModifiedBy());
			mAudit.setAuditEventDate(result.getLastModifiedDate());
			ObjectMapper oMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = oMapper.convertValue(result, Map.class);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					data.put(entry.getKey(), "");
				} else {
					data.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mAudit.setData(data);
			BannerAudit rs = bannerAuditRepository.save(mAudit);
			bannerAuditSearchRepository.save(rs);
		}
	}

}
