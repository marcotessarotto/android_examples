package it.marcot.date_dialog_example;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class MainActivity extends AppCompatActivity implements ISelectedData {

    public static final String TAG = "date_dialog_example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onSelectedData(Instant instant) {
        Log.i(TAG,"onSelectedData: " + instant);
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private ISelectedData mCallback;

        Instant today;
        Instant first_available_day;
        Instant last_available_day;
        Instant result;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            try {
                mCallback = (ISelectedData) context;
            }
            catch (ClassCastException e) {
                Log.d("MyDialog", "Context doesn't implement the ISelectedData interface");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Log.i(TAG,"RequestMeetingFragmentActivity DatePickerFragment onCreateDialog");

            // Use the current date as the default date in the picker
            today = Instant.now();
            today = today.atZone(ZoneOffset.UTC)
                    .withHour(8)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toInstant();


            first_available_day = today.plus(Duration.ofDays(7));
            last_available_day = today.plus(Duration.ofDays(7+28));

            Log.i(TAG, first_available_day.toString());
            Log.i(TAG, last_available_day.toString());

            LocalDate localDate = first_available_day.atZone(ZoneOffset.UTC).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();

//            Log.i(TAG, ""+ month + " " + day);


            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);


            dialog.getDatePicker().setMinDate(first_available_day.getEpochSecond()*1000L);
            dialog.getDatePicker().setMaxDate(last_available_day.getEpochSecond()*1000L);

            // https://developer.android.com/reference/android/app/DatePickerDialog#updateDate(int,%20int,%20int)
            // month 	int: the month (0-11 for compatibility with Calendar#MONTH)
            dialog.updateDate(year, month-1, day);

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            result = Instant.now();

            result = result.atZone(ZoneOffset.UTC)
                    .withYear(year)
                    .withMonth(month+1)
                    .withDayOfMonth(day)
                    .withHour(8)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toInstant();

            Instant instant = result;

            Log.i(TAG,"onDateSet - " + instant);

            if (mCallback != null)
                mCallback.onSelectedData(instant);

        }
    }


    public void showPickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "showPickerDialog");
    }

}