package com.davita.service;

import com.davita.exception.NotFoundException;
import com.davita.model.Content;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Created by kmasood on 7/11/16.
 */
public interface ContentProvider {
    List<Content> getContentList(String clientRole, String clientPlatform, String contentType);
    Content getContent(String contentId) throws NotFoundException;
    InputStream getContentAttachment(String contentId) throws NotFoundException;
    InputStream getContentAttachmentPreview(String contentId) throws NotFoundException;
}
