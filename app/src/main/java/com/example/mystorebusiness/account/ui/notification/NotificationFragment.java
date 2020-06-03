package com.example.mystorebusiness.account.ui.notification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystorebusiness.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class NotificationFragment extends Fragment {
    private Dialog dialog;
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private TextView empty;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        appDatabase = AppDatabase.geApp_database(getActivity());


        empty = v.findViewById(R.id.empty);

        FloatingActionButton add= v.findViewById(R.id.floatingButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReminder();
            }
        });

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        setItemsInRecyclerView();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cont_stock_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void addReminder() {

        dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select, add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME", System.currentTimeMillis() + "");
                                if (newDate.getTimeInMillis() - tem.getTimeInMillis() > 0)
                                    textView.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(getActivity(), "Invalid time", Toast.LENGTH_SHORT).show();

                            }
                        }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);
                        time.show();

                    }
                }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                RoomDAO roomDAO = appDatabase.getRoomDAO();
                Reminders reminders = new Reminders();
                reminders.setMessage(message.getText().toString().trim());
                Date remind = new Date(textView.getText().toString().trim());
                reminders.setRemindDate(remind);
                roomDAO.Insert(reminders);
                List<Reminders> l = roomDAO.getAll();
                reminders = l.get(l.size() - 1);
                Log.e("ID change", reminders.getId() + "");

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                calendar.setTime(remind);
                calendar.set(Calendar.SECOND, 0);
                Intent intent = new Intent(getActivity(), NotifierAlarm.class);
                intent.putExtra("Message", reminders.getMessage());
                intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                intent.putExtra("id", reminders.getId());
                PendingIntent intent1 = PendingIntent.getBroadcast(getActivity(), reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
                Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

                Toast.makeText(getActivity(), "Inserted Successfully", Toast.LENGTH_SHORT).show();
                setItemsInRecyclerView();
                AppDatabase.destroyInstance();
                dialog.dismiss();

            }
        });


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void setItemsInRecyclerView() {

        RoomDAO dao = appDatabase.getRoomDAO();
        List<Reminders> temp = dao.orderTheTable();
        if (temp.size() > 0) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        AdapterReminders adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);

    }
}