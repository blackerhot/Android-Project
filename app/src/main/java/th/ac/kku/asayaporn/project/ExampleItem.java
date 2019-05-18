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

    public String getTimestart() {
        return timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public String getDatest() {
        return datest;
    }

    public String getDateend() {
        return dateend;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getAddress() {
        return address;
    }
}