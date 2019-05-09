package th.ac.kku.asayaporn.project;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class calendarFragment extends Fragment {
    CompactCalendarView compactCalendarView;
    Button btn_add_event;
    String TAG = "test";
    TextView tv_month;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle("Calender");
        ((AppCompatActivity) getActivity()).getSupportActionBar().
                setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        btn_add_event = (Button) view.findViewById(R.id.btn_add_id);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        tv_month = (TextView) view.findViewById(R.id.tv_month_id);
        Date d = new Date();
        Event ev1 = new Event(Color.GREEN, 1559152871000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);
        tv_month.setText(dateFormatForMonth.format(d.getTime()));
        List<Event> events = compactCalendarView.getEvents(1559152871000L);
        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //  createEvent();
            }
        });
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String tv = "" + dateFormatForMonth.format(firstDayOfNewMonth);
                tv_month.setText(tv);
            }
        });
        return view;

    }

   /* public void createEvent(GoogleAccountCredential mCredential) {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("R_D_Location Callendar")
                .build();


        Event event = new Event()
                .setSummary("Event- April 2016")
                .setLocation("Dhaka")
                .setDescription("New Event 1");

        DateTime startDateTime = new DateTime("2016-04-17T18:10:00+06:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Dhaka");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2016-04-17T18:40:00+06:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Dhaka");
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail("abir@aksdj.com"),
                new EventAttendee().setEmail("asdasd@andlk.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            event = service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Event created: %s\n", ((com.google.api.services.calendar.model.Event) event).getHtmlLink());

    } */



}