package com.sehalsein.alzarcapartment.Model;

/**
 * Created by sehalsein on 12/11/17.
 */

public class NotificationDetail {

    private String id;
    private String title;
    private String message;
    private String timeStamp;
    private String topic;

    public NotificationDetail(String title, String message, String timeStamp) {
        this.title = title;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public NotificationDetail(String id, String title, String message, String timeStamp, String topic) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timeStamp = timeStamp;
        this.topic = topic;
    }

    public NotificationDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
