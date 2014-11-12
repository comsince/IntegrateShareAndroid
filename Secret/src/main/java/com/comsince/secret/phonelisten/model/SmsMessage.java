package com.comsince.secret.phonelisten.model;

/**
 * Created by liaojinlong on 14-11-12.
 */
public class SmsMessage {
    public static final String ADDRESS = "address";
    public static final String DATE = "date";
    public static final String BODY = "body";
    public static final String TYPE = "type";
    public static final String PERSON = "person";
    /**
     *短信地址
     * */
    private String address;
    /**
     * 短信时间
     * */
    private String date;
    /**
     * 短信内容
     * */
    private String body;
    /**
     * 短信类型，发送0,接收1
     * */
    private String type;
    /**
     *
     * */
    private String person;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", body='" + body + '\'' +
                ", type='" + type + '\'' +
                ", person='" + person + '\'' +
                '}';
    }
}
