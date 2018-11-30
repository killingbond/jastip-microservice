package com.cus.jastip.uaa.config.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.cus.jastip.uaa.messaging.model.ProfileMessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ProfileSerializer implements Serializer<ProfileMessageModel> {

	/*
	 * Author : aditya P Rulian, funggsional : Serializer ProfileMessageModel ,
	 * tanggal : 30-11-2018
	 */
	public byte[] serialize(String arg0, ProfileMessageModel obj) {
		byte[] serializedBytes = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			serializedBytes = objectMapper.writeValueAsString(obj).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serializedBytes;
	}

	public void close() {
		// TODO Auto-generated method stub
	}

	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
	}
}
