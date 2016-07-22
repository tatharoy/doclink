package com.davita.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davita.model.Content;
import com.davita.service.ContentService;

/**
 * Created by kmasood on 7/1/16.
 */
@RestController
public class ContentController {

	private static final String PARAM_USER_ROLE = "userRole";
	private static final String PARAM_USER_PLATFORM = "platform";
	private static final String PARAM_CONTENT_TYPE = "contentType";

	private static final String PARAM_CONTENT_NEED_TO_KNOW = "NeedToKnow";

	private static final String PARAM_USER_ROLE_ALL = "All";

	private static final String PARAM_PLATFORM_ALL = "All";

	private static final String PARAM_DEFAULT_CONTENT_TYPE = PARAM_CONTENT_NEED_TO_KNOW;
	private static final String PARAM_DEFAULT_USER_ROLE = PARAM_USER_ROLE_ALL;
	private static final String PARAM_DEFAULT_PLATFORM = PARAM_PLATFORM_ALL;

	@Autowired
	private ContentService contentService;

	@RequestMapping("/contents")
	public List<Content> content(
			@RequestParam(value = PARAM_USER_ROLE, defaultValue = PARAM_DEFAULT_USER_ROLE) String userRole,
			@RequestParam(value = PARAM_USER_PLATFORM, defaultValue = PARAM_DEFAULT_PLATFORM) String platform,
			@RequestParam(value = PARAM_CONTENT_TYPE, defaultValue = PARAM_DEFAULT_CONTENT_TYPE) String contentType) {

		return (getContentList(userRole, platform, contentType));
	}

	private List<Content> getContentList(String clientRole, String clientPlatform, String contentType) {

		return contentService.getContentList(clientRole, clientPlatform, contentType);
	}

	@RequestMapping("/contents/{contentId}")
	private Content getContent(@PathVariable String contentId) {

		return contentService.getContent(contentId);
	}

	@RequestMapping("/contents/{contentId}/attachment")
	private ResponseEntity<InputStreamResource> getContentAttachment(@PathVariable String contentId) {

		InputStream inputStream = contentService.getContentAttachment(contentId);

		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(inputStream));
	}

}
