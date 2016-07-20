package com.davita.service;

import com.davita.model.Content;

import java.io.InputStream;
import java.util.List;

/**
 * Created by kmasood on 7/11/16.
 */
public interface ContentProvider {
    List<Content> getContentList(String clientRole, String clientPlatform, String contentType);
    Content getContent(String contentId);
    InputStream getContentAttachment(String contentId);
    InputStream getContentAttachmentPreview(String contentId);
}
