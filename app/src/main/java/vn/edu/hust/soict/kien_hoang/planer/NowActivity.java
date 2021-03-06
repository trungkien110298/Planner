package vn.edu.hust.soict.kien_hoang.planer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class NowActivity extends Activity {
    private TextView taskName;
    private TextView startTime;
    private TextView finishTime;
    private TextView timeLeft;
    private TaskHelper taskHelper;
    private Task task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now);
        taskHelper = new TaskHelper(this);

        //Task's information
        taskName = findViewById(R.id.nowTaskName);
        startTime = findViewById(R.id.nowTaskStartTime);
        finishTime = findViewById(R.id.nowTaskFinishTime);
        timeLeft = findViewById(R.id.nowTaskTimeLeft);


        // Add new task button
        Button addTaskButton = findViewById(R.id.editCurrentTaskBtn);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTaskIntent = new Intent(NowActivity.this, NewTaskActivity.class);
                startActivity(newTaskIntent);

            }
        });


        // Update task every minutes
        Runnable update = new UpdateTask();
        Thread threadUpdate = new Thread(update);
        threadUpdate.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskHelper.close();
    }

    public void updateTask() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    long lTimeLeft = 0, lFinishTime, lStartTime, lTimeLast;
                    Calendar calendar = Calendar.getInstance();
                    String sCurrentTime = new Time(calendar.getTimeInMillis()).toString();
                    long lCurrentTime = Time.valueOf(sCurrentTime).getTime();

                    Log.d("KKKKKK", sCurrentTime + lCurrentTime);


                    int currentYear = calendar.get(Calendar.YEAR);
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    String sCurrentDate = new Date(currentYear - 1900, currentMonth, currentDay).toString();

                    if (task != null) {
                        lFinishTime = Time.valueOf(task.getFinishTime()).getTime();
                        lTimeLeft = lFinishTime - lCurrentTime;


                        if (lTimeLeft < 0) {
                            task = null;
                            taskName.setText("None");
                            startTime.setText("");
                            finishTime.setText("");
                            timeLeft.setText("");
                        }
                    }

                    //Add new task if have
                    if (task != null) {
                        timeLeft.setText(new Time(lTimeLeft - 8 * 60 * 60 * 1000).toString().substring(0, 5));
                    } else {
                        Cursor c = taskHelper.getAll();
                        c.moveToFirst();
                        do {
                            Log.d("Cursor", "" + taskHelper.getName(c));
                            String sTaskDate = taskHelper.getDate(c).toString();
                            if (sCurrentDate.equals(sTaskDate)) {
                                if (sCurrentDate.equals(sTaskDate)) {
                                    Log.d("Equals", "YOLOOOOOOO");
                                    lFinishTime = taskHelper.getFinishTime(c).getTime();
                                    lStartTime = taskHelper.getStartTime(c).getTime();
                                    Log.d("FFFFFF", taskHelper.getFinishTime(c) + " " + lFinishTime);
                                    Log.d("SSSSSS", taskHelper.getStartTime(c) + " " + lStartTime);
                                    lTimeLeft = lFinishTime - lCurrentTime;
                                    lTimeLast = lCurrentTime - lStartTime;
                                    if (lTimeLast > 0 && lTimeLeft > 0) {
                                        //Tạo công việc
                                        Log.d("Time ", "" + new Time(lFinishTime - lStartTime).toString());
                                        if (lTimeLast > 0 && lTimeLeft > 0) {
                                            //Create task
                                            Log.d("Time ", taskHelper.getFinishTime(c) + " " + taskHelper.getStartTime(c) + " "
                                                    + new Time(lFinishTime - lStartTime - 8 * 60 * 60 * 1000).toString());
                                            task = new Task();
                                            task.setName(taskHelper.getName(c));
                                            task.setStartTime(taskHelper.getStartTime(c).toString());
                                            task.setFinishTime(taskHelper.getFinishTime(c).toString());
                                            task.setDate(sTaskDate);

                                            //Cập nhật lên giao diện
                                            taskName.setText(taskHelper.getName(c));
                                            startTime.setText(taskHelper.getStartTime(c).toString().substring(0, 5));
                                            finishTime.setText(taskHelper.getFinishTime(c).toString().substring(0, 5));
                                            timeLeft.setText(new Time(lTimeLeft - 8 * 60 * 60 * 1000).toString().substring(0, 5));
                                            break;
                                        }
                                    }
                                }
                            }}}
                        } while (c.moveToNext());


                        if (task == null) {
                            taskName.setText("None");
                            startTime.setText("--:--");
                            finishTime.setText("--:--");
                            timeLeft.setText("--:--");
                        }
                        // Bắt ngoại lệ
                    }
                } catch (Exception e) {
                    // Exception
                }
            }
        });
    }


    /**
     * Class to update task every minute
     */

    class UpdateTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    updateTask();
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

}
