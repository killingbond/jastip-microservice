package com.cus.jastip.master.service;

import java.io.IOException;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageProcessService {

	private static final String CLOUDINARY_CLOUD_NAME = "drdrxjaif";
	private static final String CLOUDINARY_API_KEY = "667642892298513";
	private static final String CLOUDINARY_API_SECRET = "xxCK7QupUAKGbYmeVrSqutp-gxU";
	Cloudinary cloudinary = getCloudinaryClient();

	/*
	 * Author : aditya P Rulian, funggsional : Get byte image , tanggal : 30-11-2018
	 */
	public String onSubmit(byte[] bs) throws Exception {

		@SuppressWarnings("unused")
		String fileName = "";

		Map<String, Object> cloudinaryURL = null;

		if (bs != null) {
			try {
				fileName = bs.toString();
				cloudinaryURL = uploadToCloudinary(cloudinary, bs);
			} catch (Exception e) {
				return "fail";
			}

		}

		System.out.println("Clodinary URL: " + (String) cloudinaryURL.get("public_id"));

		return bs.toString();

	}

	/*
	 * Author : aditya P Rulian, funggsional : Building image url , tanggal :
	 * 30-11-2018
	 */
	public String urlImage(String uri) {
		String url = cloudinary.url().format("jpg").transformation(new Transformation())
				.generate("mm_images/master/" + uri.toString().split("\\.", 3)[0]);
		return url;
	}

	/*
	 * Author : aditya P Rulian, funggsional : Building image url Thumb , tanggal :
	 * 30-11-2018
	 */
	public String urlImageThumb(String uri) {
		String url = cloudinary.url().format("jpg").transformation(new Transformation().width(150).height(150))
				.generate("mm_images/master/" + uri.toString().split("\\.", 3)[0]);
		return url;
	}

	private static Cloudinary getCloudinaryClient() {

		return new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUDINARY_CLOUD_NAME, "api_key", CLOUDINARY_API_KEY,
				"api_secret", CLOUDINARY_API_SECRET, "secure", true));
	}

	/*
	 * Author : aditya P Rulian, funggsional : Upload Image to Cloudinary , tanggal
	 * : 30-11-2018
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> uploadToCloudinary(Cloudinary cloudinary, byte[] sourceFile) throws IOException {

		String filename = sourceFile.toString();
		Map<String, Object> cloudinaryUrl = null;
		Map params = ObjectUtils.asMap("public_id", "mm_images/master/" + filename.split("\\.", 3)[0]);

		// Convert multipart file type image to File type because Cloudinary doesn't
		// accept multipart file type.

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().upload(sourceFile, params);
			cloudinaryUrl = result;

		} catch (IOException e) {
			System.out.println("Could not upload file to Cloundinary from Byte[] " + filename + e.toString());
			throw e;
		}

		return cloudinaryUrl;
	}

}
