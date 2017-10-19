package io.hasura.orange;

/**
 * Created by HARIHARAN on 14-10-2017.
 */

public class Article {

    String title;
    String subtitle;
    String content;

    public Article(String title, String subtitle, String content) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
