package com.example.courses.data.model;

public class Lecture {
    private String id;
    private String course_id;
    private String title;
    private String content;
    private String video_url;

    public Lecture() {
    }

    public Lecture(String id, String course_id, String title, String content, String video_url) {
        this.id = id;
        this.course_id = course_id;
        this.title = title;
        this.content = content;
        this.video_url = video_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
