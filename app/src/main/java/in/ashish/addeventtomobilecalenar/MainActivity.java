package in.ashish.addeventtomobilecalenar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALENDAR = 99;
    private Button btnAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent(getApplicationContext(), "Running for the challenge");
            }
        });
    }

    public void addEvent(Context ctx, String title) {
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        Date Startdate = null;
        Date Enddate = null;
        String dtStart = "22/02/2017";
        try {
            Startdate = df2.parse(dtStart);
            Enddate = df2.parse("28/02/2017");
            Log.v("SDate: ", "" + df3.format(Startdate));
            Log.v("EDate: ", "" + df3.format(Enddate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cali = Calendar.getInstance();
        cali.setTime(Startdate);


        Calendar cali2 = Calendar.getInstance();
        cali2.setTime(Enddate);

        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
        Calendar dt = Calendar.getInstance();


        dt.setTime(Enddate);

        String dtUntill = yyyymmdd.format(dt.getTime());

        ContentResolver contentResolver = ctx.getContentResolver();

        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, title);
//        calEvent.put(CalendarContract.Events.RRULE, "FREQ=MONTHLY;INTERVAL=1;UNTIL=" + dtUntill);
        calEvent.put(CalendarContract.Events.DTSTART, cali.getTimeInMillis());
        Log.e("DTSTART", "Ask :  " + cali.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_LOCATION, "India , Madhya Pradesh , Indore");
        calEvent.put(CalendarContract.Events.DESCRIPTION, "We’ve all been there. Lacing up for the first time ever or lacing up after taking a long period off is rough. That first run always makes you wonder “Why am I doing this?” At times you want to quit, but you want the benefits that come from running. <strong>The key to great running and increasing your mileage is to be on your feet as long as possible. </strong>Our 7-day Beginner’s Running Challenge will make a you fall in love with running by day 3!");
        calEvent.put(CalendarContract.Events.DTEND, cali2.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "" + java.util.Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(ctx, "IF", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calEvent);
        int id = Integer.parseInt(uri.getLastPathSegment());
        Toast.makeText(ctx, "Created Calendar Event " + id,
                Toast.LENGTH_SHORT).show();
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID, id);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminders.put(CalendarContract.Reminders.MINUTES, 10);
//        Uri uri1 = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_CALENDAR)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_CALENDAR);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_CALENDAR);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_CALENDAR)
                            == PackageManager.PERMISSION_GRANTED) {
//                        addEvent(getApplicationContext(), "Running for the challenge");
                    }
                    Toast.makeText(this, "permission allow", Toast.LENGTH_LONG).show();
                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
