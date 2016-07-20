package com.davita.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by kmasood on 7/1/16.
 */
@Entity
public class Content {
    @Id
    private String id;
    private String headline;
    private String details;

    private Content() {}

    public Content(String cmsId, String headline, String details) {
        this.id = cmsId;
        this.headline = headline;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format(
                "Content[id='%s', headline='%s', details='%s']",
                id, headline, details);
    }
}
