package th.ac.kku.asayaporn.project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ExampleItem {
    private String timestart;
    private String timeend;
    private String datest;
    private String dateend;
    private String title;
    private String content;
    private String place;
    public ExampleItem(String timeStart, String timeEnd,String dateStart,String dateEnd, String title ,String content,String place) {
        this.timestart = timeStart;
        this.timeend = timeEnd;
        this.datest = dateStart;
        this.dateend = dateEnd;
        this.title = title;
        this.content = content;
        this.place = place;
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

    public String getDateStart() {
        return datest;
    }

    public void setDateStart(String dateStart) {
        this.datest = dateStart;
    }

    public String getDateEnd() {
        return dateend;
    }

    public void setDateEnd(String dateEnd) {
        this.dateend = dateEnd;
    }

    public String getTimeStart() {
        return timestart;
    }

    public void setTimeStart(String timeStart) {
        this.timestart = timeStart;
    }

    public String getTimeEnd() {
        return timeend;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeend = timeEnd;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("timestart", timestart);
        result.put("timeend", timeend);
        result.put("datest", datest);
        result.put("dateend", dateend);
        result.put("title", title);
        result.put("content", content);
        result.put("place", place);
        return result;
    }
}