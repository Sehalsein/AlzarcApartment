package com.sehalsein.alzarcapartment.Model;

/**
 * Created by sehalsein on 12/11/17.
 */

public class MeetingDetail {

    private String id;
    private String title;
    private String message;
    private String timeStamp;
    private String topic;
    private String results;

    public MeetingDetail() {
    }

    public MeetingDetail(String title, String message, String timeStamp) {
        this.title = title;
        this.message = message;
        this.timeStamp = timeStamp;
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

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
