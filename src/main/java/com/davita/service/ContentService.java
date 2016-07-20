package com.davita.service;

import com.davita.exception.ApplicationException;
import com.davita.exception.NotFoundException;
import com.davita.model.Content;

import java.io.InputStream;
import java.util.List;

/**
 * Created by kmasood on 7/20/16.
 */
public interface ContentService {

    List<Content> getContentList(String clientRole, String clientPlatform, String contentType) throws ApplicationException;
    Content getContent(String contentId) throws NotFoundException, ApplicationException;
    InputStream getContentAttachment(String contentId) throws NotFoundException, ApplicationException;
}
