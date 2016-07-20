package com.davita.service;

import com.davita.exception.NotFoundException;
import com.davita.model.Content;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.gitana.platform.client.Gitana;
import org.gitana.platform.client.attachment.Attachment;
import org.gitana.platform.client.branch.Branch;
import org.gitana.platform.client.node.Node;
import org.gitana.platform.client.platform.Platform;
import org.gitana.platform.client.repository.Repository;
import org.gitana.platform.client.support.DriverContext;
import org.gitana.platform.client.support.Remote;
import org.gitana.platform.support.Pagination;
import org.gitana.platform.support.QueryBuilder;
import org.gitana.platform.support.ResultMap;
import org.gitana.platform.support.Sorting;
import org.gitana.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by kmasood on 7/11/16.
 */
@Service("ContentProvider")
public class ContentProviderImpl implements ContentProvider {

    private static final String CMS_CLIENT_KEY = "asdsadas";
    private static final String CMS_CLIENT_SECRET = "asdasds";
    private static final String CMS_USERNAME = "asdasdas";
    private static final String CMS_PASSWORD = "asdasdsadas";
    private static final String CMS_REPO_ID = "asdasd";
    private static final String CMS_REPO_BRANCH = "master";

    private static final String PARAM_CONTENT_ACTION_ITEMS = "ActionItems";
    private static final String PARAM_CONTENT_NEED_TO_KNOW = "NeedToKnow";
    private static final String PARAM_CONTENT_PHYS_MEMO = "PhysicianMemo";

    private static final String PARAM_USER_ROLE_ALL = "All";
    private static final String PARAM_USER_ROLE_MD = "MD";
    private static final String PARAM_USER_ROLE_PHYSICAIN = "Physician";
    private static final String PARAM_USER_ROLE_ATTENDEE = "Attendee";

    private static final String PARAM_PLATFORM_ALL = "All";
    private static final String PARAM_PLATFORM_WEB = "Web";
    private static final String PARAM_PLATFORM_IPAD = "iPad";
    private static final String PARAM_PLATFORM_IPHONE = "iPhone";

    private static final String PARAM_DEFAULT_USER_ROLE = PARAM_USER_ROLE_ALL;
    private static final String PARAM_DEFAULT_PLATFORM = PARAM_PLATFORM_ALL;

    private static final String CONTENT_ACTION_ITEMS_CODE = "custom:actionitem0";
    private static final String CONTENT_NEED_TO_KNOW_CODE = "custom:needtoknow";
    private static final String CONTENT_PHYS_MEMO_CODE = "custom:physicianmemo";

    private static final String CONTENT_SUBJECT_FIELD = "title";
    private static final String CONTENT_DETAILS_FIELD = "details";

    private static final String CONTENT_ID_VALIDATION_ERROR = "Invalid contentId provided: ";

    private Gitana gitana;
    private Platform platform;
    private Repository repository;
    private Branch masterRepo;

    private String username = CMS_USERNAME;
    private String password = CMS_PASSWORD;


    private void createCaaSConnction() {
        // connect as to Cloud CMS as your tenant and user
        gitana = new Gitana(CMS_CLIENT_KEY, CMS_CLIENT_SECRET);

        platform = gitana.authenticate(username, password);

        // read from the main repository
        repository = platform.readRepository(CMS_REPO_ID);

        // point to the main branch of the repository
        masterRepo = repository.readBranch(CMS_REPO_BRANCH);
    }


    @Override
    public List<Content> getContentList(String clientRole, String clientPlatform, String contentType) {

        List<Content> contents = new ArrayList<Content>();
        StringBuilder outputString = new StringBuilder();

        // I hate doing this but I have to!!! ugh
        createCaaSConnction();

        // Assign default values for nulls
        if (clientRole == null) { clientRole = PARAM_USER_ROLE_ALL; }
        if (clientPlatform == null) { clientPlatform = PARAM_PLATFORM_ALL; }
        if (contentType == null) { contentType = CONTENT_NEED_TO_KNOW_CODE; }

        String contentTypeCode;

        if (contentType.equals(PARAM_CONTENT_PHYS_MEMO)) {
            contentTypeCode = CONTENT_PHYS_MEMO_CODE;
        } else if (contentType.equals(PARAM_CONTENT_NEED_TO_KNOW)) {
            contentTypeCode = CONTENT_NEED_TO_KNOW_CODE;
        } else if (contentType.equals(PARAM_CONTENT_ACTION_ITEMS)) {
            contentTypeCode = CONTENT_ACTION_ITEMS_CODE;
        } else {
            contentTypeCode = CONTENT_NEED_TO_KNOW_CODE;
        }

        QueryBuilder qb = QueryBuilder.start("_type").is(contentTypeCode);

        String userRoleCode;

        if (clientRole.equals(PARAM_USER_ROLE_ALL)) {
            userRoleCode = PARAM_USER_ROLE_ALL;
        } else if (clientRole.equals(PARAM_USER_ROLE_ATTENDEE)) {
            userRoleCode = PARAM_USER_ROLE_ATTENDEE;
        } else if (clientRole.equals(PARAM_USER_ROLE_PHYSICAIN)) {
            userRoleCode = PARAM_USER_ROLE_PHYSICAIN;
        } else if (clientRole.equals(PARAM_USER_ROLE_MD)) {
            userRoleCode = PARAM_USER_ROLE_MD;
        } else {
            userRoleCode = PARAM_USER_ROLE_ALL;
        }

        if (!userRoleCode.equals(PARAM_DEFAULT_USER_ROLE)) {
            qb.or(createNode("targetUser", userRoleCode),
                    createNode("targetUser", PARAM_DEFAULT_USER_ROLE),
                    createNode("targetUser", null));
        } else {
            qb.or(createNode("targetUser", PARAM_DEFAULT_USER_ROLE));
        }

        String platformCode;

        if (clientPlatform.equals(PARAM_PLATFORM_ALL)) {
            platformCode = PARAM_PLATFORM_ALL;
        } else if (clientPlatform.equals(PARAM_PLATFORM_IPAD)) {
            platformCode = PARAM_PLATFORM_IPAD;
        } else if (clientPlatform.equals(PARAM_PLATFORM_IPHONE)) {
            platformCode = PARAM_PLATFORM_IPHONE;
        } else if (clientPlatform.equals(PARAM_PLATFORM_WEB)) {
            platformCode = PARAM_PLATFORM_WEB;
        } else {
            platformCode = PARAM_PLATFORM_ALL;
        }

//        if (platformCode != null && !platformCode.isEmpty()) {
        if (!platformCode.equals(PARAM_PLATFORM_ALL)) {
            qb.or(createNode("platform", platformCode),
                    createNode("platform", PARAM_DEFAULT_PLATFORM),
                    createNode("platform", null));
        } else {
            qb.or(createNode("platform", platformCode));
        }

        Sorting sorting = new Sorting();
        sorting.addSortAscending("created_on");
        Pagination pagination = new Pagination();
        pagination.setSorting(sorting);

        ObjectNode query = qb.get();
        ResultMap results = masterRepo.queryNodes(query, pagination);

        for (Object obj: results.values()) {
            Node node = (Node)obj;
            contents.add(new Content(node.getId(),
                    (String)node.get(CONTENT_SUBJECT_FIELD),
                    null));

            outputString.append(node.getId());
            outputString.append(" - ");
            outputString.append(node.get(CONTENT_SUBJECT_FIELD));
            outputString.append(node.get(CONTENT_DETAILS_FIELD));
            outputString.append(node.listAttachments());
            System.out.println("Results: " + outputString);
//                System.out.println("Results: " + node.toString());
        }

        return contents;
    }

    @Override
    public Content getContent(String contentId) throws NotFoundException {
        Content content = null;

        // I hate doing this but I have to!!! ugh
        createCaaSConnction();

        QueryBuilder qb = QueryBuilder.start("_doc").is(contentId);

        ObjectNode query = qb.get();

        ResultMap results = masterRepo.queryNodes(query);

        if (results.isEmpty()) {
            throw new NotFoundException(CONTENT_ID_VALIDATION_ERROR + contentId);
        }

        for (Object obj: results.values()) {
            Node node = (Node)obj;
            content = new Content(node.getId(),
                    (String)node.get(CONTENT_SUBJECT_FIELD),
                    (String)node.get(CONTENT_DETAILS_FIELD));
        }

        return content;
    }


    @Override
    public InputStream getContentAttachment(String contentId) throws NotFoundException {
        InputStream inputStream = null;

        // I hate doing this but I have to!!! ugh
        createCaaSConnction();

        QueryBuilder qb = QueryBuilder.start("_doc").is(contentId);

        ObjectNode query = qb.get();

        ResultMap results = masterRepo.queryNodes(query);

        if (results.isEmpty()) {
            throw new NotFoundException(CONTENT_ID_VALIDATION_ERROR + contentId);
        }

        for (Object obj: results.values()) {
            Node node = (Node)obj;

            ResultMap<Attachment> attachmentList = node.listAttachments();

            for (Attachment attachment: attachmentList.values()) {
                if (attachment.getId().equalsIgnoreCase("_preview_default_icon48_48")) {
                    inputStream = attachment.getInputStream();
                }
            }
        }

        return inputStream;
    }


    @Override
    public InputStream getContentAttachmentPreview(String contentId) throws NotFoundException {
        InputStream inputStream = null;

        Remote remote = DriverContext.getDriver().getRemote();

        // I hate doing this but I have to!!! ugh
        createCaaSConnction();

        QueryBuilder qb = QueryBuilder.start("_doc").is(contentId);

        ObjectNode query = qb.get();

        ResultMap results = masterRepo.queryNodes(query);

        if (results.isEmpty()) {
            throw new NotFoundException(CONTENT_ID_VALIDATION_ERROR + contentId);
        }

        for (Object obj: results.values()) {
            Node node = (Node)obj;

            ResultMap<Attachment> attachmentList = node.listAttachments();

            for (Attachment attachment: attachmentList.values()) {
                if (attachment.getId().equalsIgnoreCase("_preview_default_icon48_48")) {
                    inputStream = attachment.getInputStream();
                }
            }
        }

        return inputStream;
    }


    private ObjectNode createNode(String fieldName, String value) {
        ObjectNode objNode = JsonUtil.createObject();
        objNode.put(fieldName, value);

        return objNode;
    }
}
