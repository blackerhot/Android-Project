package th.ac.kku.asayaporn.project;

import com.google.firebase.database.Exclude;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityKKU {


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JsonObject getContact() {
        return contact;
    }

    public void setContact(JsonObject contact) {
        this.contact = contact;
    }

    JsonObject contact;
    String url;

    public ActivityKKU(JsonObject contact, String url, String image, String title, String place,
                       String content, String dateSt, String dateEd, String phone, String website, String timeSt, String timeEt, String sponsor) {
        this.contact = contact;
        this.url = url;
        this.image = image;
        this.title = title;
        this.place = place;
        this.content = content;
        this.dateSt = dateSt;
        this.dateEd = dateEd;
        this.phone = phone;
        this.website = website;
        this.timeSt = timeSt;
        this.timeEd = timeEt;
        this.sponsor = sponsor;
    }

    String image;
    String title;
    String place;

    String content;
    String dateSt;
    String dateEd;
    String phone;
    String website;

    public String getTimeSt() {
        return timeSt;
    }

    public void setTimeSt(String timeSt) {
        this.timeSt = timeSt;
    }

    public String getTimeEd() {
        return timeEd;
    }

    public void setTimeEd(String timeEd) {
        this.timeEd = timeEd;
    }

    String timeSt;
    String timeEd;
    String sponsor;

    public ActivityKKU(){


    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("place", place);
        result.put("image", image);
        result.put("sponsor", sponsor);
        result.put("website", website);
        result.put("dateSt",dateSt);
        result.put("dateEd",dateEd);
        result.put("content",content);
        result.put("phone",phone);
        result.put("contact",contact);
        result.put("timeSt",timeSt);
        result.put("timeEd",timeEd);
        result.put("url",url);
        return result;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateSt() {
        return dateSt;
    }

    public void setDateSt(String dateSt) {
        this.dateSt = dateSt;
    }

    public String getDateEd() {
        return dateEd;
    }

    public void setDateEd(String dateEd) {
        this.dateEd = dateEd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }












}
