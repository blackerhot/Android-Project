
package th.ac.kku.asayaporn.project;

        import com.google.firebase.database.Exclude;
        import com.google.gson.JsonObject;

        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class ActivityUser {


    public ActivityUser(String uid, String email, String classes) {
        this.uid = uid;
        this.email = email;
        this.classes = classes;
    }
    public ActivityUser(){

    }

    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    String email;
   String classes;




    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("classes", classes);
        result.put("uid", uid);

        return result;
    }




}
