package com.davita;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.gitana.platform.client.Gitana;
import org.gitana.platform.client.attachment.Attachment;
import org.gitana.platform.client.branch.Branch;
import org.gitana.platform.client.node.Node;
import org.gitana.platform.client.platform.Platform;
import org.gitana.platform.client.repository.Repository;
import org.gitana.platform.support.Pagination;
import org.gitana.platform.support.QueryBuilder;
import org.gitana.platform.support.ResultMap;
import org.gitana.platform.support.Sorting;
import org.gitana.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private ContentProvider contentProvider;

    @RequestMapping("/contents")
    public List<Content> content(@RequestParam(value=PARAM_USER_ROLE, defaultValue= PARAM_DEFAULT_USER_ROLE) String userRole,
                                 @RequestParam(value=PARAM_USER_PLATFORM, defaultValue= PARAM_DEFAULT_PLATFORM) String platform,
                                 @RequestParam(value=PARAM_CONTENT_TYPE, defaultValue= PARAM_DEFAULT_CONTENT_TYPE) String contentType) {

        return (getContentList(userRole, platform, contentType));
    }


    private List<Content> getContentList(String clientRole, String clientPlatform, String contentType) {

        List<Content> contents;

        try {
//            ObjectNode query = QueryBuilder.start("_type").is("custom:needtoknow").get();
//            ObjectNode query = QueryBuilder.start("_type").is("custom:actionitem0").get();

            contents = contentProvider.getContentList(clientRole, clientPlatform, contentType);

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

        return contents;
    }


    @RequestMapping("/contents/{contentId}")
    private Content getContent(@PathVariable String contentId) {

        Content content = null;

        try {
            content = contentProvider.getContent(contentId);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }

        return content;
    }


    @RequestMapping("/contents/{contentId}/attachment")
    private ResponseEntity<InputStreamResource> getContentAttachment(@PathVariable String contentId) {

        InputStream inputStream = null;

        try {
            inputStream = contentProvider.getContentAttachment(contentId);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(inputStream));
    }

}
