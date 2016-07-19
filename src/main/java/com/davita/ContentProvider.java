package com.davita;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Created by kmasood on 7/11/16.
 */
public interface ContentProvider {
    List<Content> getContentList(String clientRole, String clientPlatform, String contentType);
    Content getContent(String contentId);
    InputStream getContentAttachment(String contentId);
}
