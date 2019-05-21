package th.ac.kku.asayaporn.project;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private ItemActivity iActivity;
    boolean checkSummary = false;
    String uri = null;
    List<String> eventStrings;
    Intent intent;
    Bundle para;

    /**
     * Constructor.
     *
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(ItemActivity activity) {
        this.iActivity = activity;
        para = iActivity.getIntent().getExtras();

    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {

            if(iActivity.create) {
                createEvent();
            }

            getDataFromApi();

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            iActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            iActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    ItemActivity.REQUEST_AUTHORIZATION);
        }  catch (IOException e) {
            System.out.printf("The following error occurred: " +
                    e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        final String link = uri;
        Log.d("TAGcal", link + "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = new GregorianCalendar();
        try {

            if (link != null) {
                Date date = sdf.parse(link);
                cal.setTime(date);
                cal.add(Calendar.MONTH, 0);
                long time = cal.getTime().getTime();
                Uri.Builder builder =
                        CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                builder.appendPath(Long.toString(time));
                intent = new Intent(Intent.ACTION_VIEW, builder.build());

                if (iActivity.create) {
                    View rootView = iActivity.findViewById(R.id.linearLayout2);
                    Snackbar.make(rootView, "already add to calendar", Snackbar.LENGTH_LONG).
                            setAction("Click to Open", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            iActivity.startActivity(intent);
                        }
                    })
                            .setActionTextColor(Color.YELLOW)
                            .show();
                }

                if (checkSummary) {
                    iActivity.butAddEvent.setText("Already Added");
                    iActivity.butAddEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //                    Intent openBowser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            //                    iActivity.startActivity(openBowser);
                            iActivity.startActivity(intent);
                        }
                    });
                }
            }




        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void createEvent() throws IOException {

        Event event = new Event()
                .setSummary(para.getString("title"))
                .setLocation(para.getString("address"))
                .setDescription(para.getString("detail"));


        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        String str  = para.getString("timest");
        String word = str.substring(0,4);
        str=str.replace(word,"");
        str=str.replace(".",":");
       word= word.replace(". ","");
        str=str+" "+word;
        Date date =null;


        try {
            date = parseFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }DateTime startDateTime = null;
        try {
            startDateTime   = new DateTime(para.getString("datest")+"T"+displayFormat.format(date)+":00.000");
        } catch (Exception e) {
            return;
        }

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        start.setTimeZone(start.getTimeZone());
        event.setStart(start);

        String str1  = para.getString("timest");
        String word1 = str1.substring(0,4);
        str1=str1.replace(word1,"");
        str1=str1.replace(".",":");
        word1= word1.replace(". ","");
        str1=str1+" "+word1;
        try {
            date = parseFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateTime endDateTime = new DateTime(para.getString("dateend")+"T"+displayFormat.format(date)+":00.000");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        end.setTimeZone(end.getTimeZone());
        event.setEnd(end);


        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(24 * 60),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
        String calendarId = "primary";
        try {
            event = iActivity.mService.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private List<String> getDataFromApi() throws IOException {

        eventStrings = new ArrayList<String>();
        Events events = iActivity.mService.events().list("primary")
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();
        for (final Event event : items) {
            String getSummary = event.getSummary();
            EventDateTime eventDateTime = event.getStart();
            String getTAG = event.getDescription();
            if (getTAG != null ) {
                eventStrings.add(String.format("%s (%s)", event.getSummary(), getTAG));
            }
            if (getSummary != null && getSummary.equals(para.getString("title"))) {
                uri = eventDateTime.getDateTime().toString();
                uri = uri.substring(0, 10);
                checkSummary = true;
                break;
            }
        }

        return eventStrings;
    }

}