package th.ac.kku.asayaporn.project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ExampleItem {
    private String timestart;

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getDatest() {
        return datest;
    }

    public void setDatest(String datest) {
        this.datest = datest;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String timeend;
    private String datest;
    private String dateend;
    private String title;
    private String detail;
    private String address;
    public ExampleItem(String timeStart, String timeEnd,String dateStart,String dateEnd, String title ,String content,String place) {
        this.timestart = timeStart;
        this.timeend = timeEnd;
        this.datest = dateStart;
        this.dateend = dateEnd;
        this.title = title;
        this.detail = content;
        this.address = place;
    }

    public ExampleItem(){

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("timestart", timestart);
        result.put("timeend", timeend);
        result.put("datest", datest);
        result.put("dateend", dateend);
        result.put("title", title);
        result.put("detail", detail);
        result.put("address", address);
        return result;
    }
}