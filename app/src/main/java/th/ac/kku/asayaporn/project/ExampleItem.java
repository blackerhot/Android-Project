package th.ac.kku.asayaporn.project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ExampleItem {
    private String timeStart;
    private String timeEnd;
    private String dateStart;
    private String dateEnd;
    private String title;
    private String content;
    private String place;
    public ExampleItem(String timeStart, String timeEnd,String dateStart,String dateEnd, String title ,String content,String place) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
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
        result.put("timeStart", timeStart);
        result.put("timeEnd", timeEnd);
        result.put("dateStart", dateStart);
        result.put("dateEnd", dateEnd);
        result.put("title", title);
        result.put("content", content);
        result.put("place", place);
        return result;
    }
}