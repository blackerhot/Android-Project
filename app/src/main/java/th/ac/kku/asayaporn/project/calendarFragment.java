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




}