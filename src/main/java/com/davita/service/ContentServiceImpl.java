package com.davita.service;

import com.davita.exception.ApplicationException;
import com.davita.exception.NotFoundException;
import com.davita.model.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.InputStream;
import java.util.List;

/**
 * Created by kmasood on 7/20/16.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentProvider contentProvider;

    @Override
    public List<Content> getContentList(String clientRole, String clientPlatform, String contentType) throws ApplicationException {

        List<Content> contents = contentProvider.getContentList(clientRole, clientPlatform, contentType);

        return contents;
    }

    @Override
    public Content getContent(String contentId) throws NotFoundException, ApplicationException {

        Content content = contentProvider.getContent(contentId);

        return content;
    }

    @Override
    public InputStream getContentAttachment(String contentId) throws NotFoundException, ApplicationException {

        InputStream inputStream = contentProvider.getContentAttachment(contentId);

        return inputStream;
    }
}
