package com.example.notebook.enity;

/**
 * create_by Android Studio
 *
 * @author zouguo0212@
 * @package_name fun.zzti.enity
 * @description
 * @date 2018/10/26 19:11
 */
public class Record {
    private Integer id;  //记事本id
    private String titleName;  //题目名字
    private String textBody;  //事件主体
    private String author; //作者
    private String createTime;  //创建时间
    private String noticeTime;    //提醒时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", titleName='" + titleName + '\'' +
                ", textBody='" + textBody + '\'' +
                ", author='" + author + '\'' +
                ", createTime='" + createTime + '\'' +
                ", noticeTime='" + noticeTime + '\'' +
                '}';
    }
}
