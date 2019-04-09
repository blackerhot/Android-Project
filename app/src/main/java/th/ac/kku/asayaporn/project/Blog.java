package th.ac.kku.asayaporn.project;

import java.util.List;

public class Blog {


    public Blog(List<ActivityKKU> activities) {
        this.activities = activities;
    }

    public List<ActivityKKU> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityKKU> activities) {
        this.activities = activities;
    }

    List<ActivityKKU> activities;






}
