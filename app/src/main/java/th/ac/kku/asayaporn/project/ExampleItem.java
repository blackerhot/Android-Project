package th.ac.kku.asayaporn.project;

public class ExampleItem {
    private String timestart;
    private String timeend;
    private String datest;
    private String dateend;
    private String title;
    private String detail;
    private String address;
    public ExampleItem(String timestart1, String timeend1,String datest1,String dateend1, String title1 ,String detail1,String address1) {
        timestart = timestart1;
        timeend = timeend1;
        datest = datest1;
        dateend = dateend1;
        title = title1;
        detail = detail1;
        address = address1;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setDatest(String datest) {
        this.datest = datest;
    }

    public String getDatest() {
        return datest;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getDateend() {
        return dateend;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}