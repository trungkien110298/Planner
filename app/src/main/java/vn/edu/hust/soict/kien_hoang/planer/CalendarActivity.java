package vn.edu.hust.soict.kien_hoang.planer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity extends Activity {
    CalendarView simpleCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        simpleCalendarView = findViewById(R.id.calendarView); // Lấy mã của calendarView để thao tác

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { // Nếu có đổi ngày thì sẽ chuyển sang ngày đó ở tab Day
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
//                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("date",dayOfMonth + "/" + month + "/" + year);
//                bundle.putString("routes","Day");
//                intent.putExtra("bundle",bundle);
//                startActivity(intent);
                MainActivity.setCurrentTabHost(1);
            }
        });
    }
}
