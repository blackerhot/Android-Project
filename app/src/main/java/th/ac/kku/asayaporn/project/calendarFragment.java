package th.ac.kku.asayaporn.project;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;

public class calendarFragment extends Fragment {
    MaterialCalendarView materialCalendar;
    TextView tv_month;
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        getActivity().setTitle("Calender");
        ((AppCompatActivity)getActivity()).getSupportActionBar().
                setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        materialCalendar = (MaterialCalendarView) view.findViewById(R.id.materialcalendarview_id);
        tv_month = (TextView) view.findViewById(R.id.tv_month_id);

        materialCalendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                    String calendar_str = "" + calendarDay.getMonth();

               // String month_num_str = String.valueOf(month_char_one + month_char_two);

                   checkmonth(calendar_str);
            }
        });
        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

            }
        });
        return view;

    }

    private void checkmonth(String month_num_str) {

        switch(month_num_str.toLowerCase()) {
            case "0":
                tv_month.setText("January");
                break;
            case "1":
                tv_month.setText("February");
                break;
            case "2":
                tv_month.setText("March");
                break;
            case "3":
                tv_month.setText("April");
                break;
            case "4":
                tv_month.setText("May");
                break;
            case "5":
                tv_month.setText("June");
                break;
            case "6":
                tv_month.setText("July");
                break;
            case "7":
                tv_month.setText("August");
                break;
            case "8":
                tv_month.setText("September");
                break;
            case "9":
                tv_month.setText("October");
                break;
            case "10":
                tv_month.setText("November");
                break;
            case "11":
                tv_month.setText("December");
                break;
            default:

        }

    }

}