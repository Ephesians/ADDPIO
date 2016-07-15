package com.blahblah.addpio;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import static com.blahblah.addpio.Types.*;

public class MainActivity extends Activity {
    private static final int NOTIFICATION_ID = 999;
    private static final int TOUCH_PAD_SIZE = 480;
    private static final int POSITIONER_SIZE = 8;
    private static final int IO_INDICATOR_DURATION = 500;
    private static final List<Integer> OUTPUT_PINS = Arrays.asList(LED_RED, LED_GREEN, LED_BLUE, ALARM, NOTIFICATION, TEXT, TOUCH_PAD_X_OUT, TOUCH_PAD_Y_OUT);
    private Handler mHandler;
    private TextView infoip,textView;
    private Button button1, button2;
    private ImageView LEDred, LEDgreen, LEDblue, positioner, in_indicator, out_indicator;
    private int touchpadX, touchpadY;
    private Uri mNotification;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_main);
        infoip = (TextView) findViewById(R.id.infoip);
        infoip.setText(getIpAddress());
        ImageView sync = (ImageView) findViewById(R.id.sync);
        sync.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        infoip.setText(getIpAddress());
                    }
                }
        );
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        LEDred = (ImageView) findViewById(R.id.LEDred);
        LEDgreen = (ImageView) findViewById(R.id.LEDgreen);
        LEDblue = (ImageView) findViewById(R.id.LEDblue);
        textView = (TextView) findViewById(R.id.text);
        ImageView touchpad = (ImageView) findViewById(R.id.touchpad);
        positioner = (ImageView) findViewById(R.id.positioner);
        in_indicator = (ImageView) findViewById(R.id.in_indicator);
        out_indicator = (ImageView) findViewById(R.id.out_indicator);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_DOWN) {
                    double x = event.getX();
                    double y = event.getY();
                    positioner.setX((float)x - POSITIONER_SIZE / 2);
                    positioner.setY((float)y - POSITIONER_SIZE / 2);
                    touchpadX = (int)Math.round(x * 256.0 / TOUCH_PAD_SIZE);
                    touchpadY = (int)Math.round(y * 256.0 / TOUCH_PAD_SIZE);
                }
                return false;
            }
        });
        mNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        SensorStorage.getInstance().init((SensorManager) getSystemService(SENSOR_SERVICE), this);
        new UdpServer(this).start();
    }

    String getInput(int pinNumber, int index) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                in_indicator.setAlpha(1.0f);
                mHandler.postDelayed(new Runnable() {public void run() {in_indicator.setAlpha(0.0f);}}, IO_INDICATOR_DURATION);
            }
        });
        String response;
        if (pinNumber == BUTTON_1) {
            response = button1.isPressed() ? "1" : "0";
        } else if (pinNumber == BUTTON_2) {
            response = button2.isPressed() ? "1" : "0";
        } else if (pinNumber == TOUCH_PAD_X_IN) {
            response = "" + touchpadX;
        } else if (pinNumber == TOUCH_PAD_Y_IN) {
            response = "" + touchpadY;
        } else {
            response = SensorStorage.getInstance().getSensorValue(pinNumber, index);
        }
        return response;
    }

    String setOutput(final int pinNumber, final int extraNumber) {
        String response = getString(R.string.output_success);
        if (OUTPUT_PINS.indexOf(pinNumber) == -1) {
            response = getString(R.string.bad_pin_number);
        }
        else if (pinNumber == ALARM && mNotification == null) {
            response = getString(R.string.no_alarm);
        } else {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    out_indicator.setAlpha(1.0f);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            out_indicator.setAlpha(0.0f);
                        }
                    }, IO_INDICATOR_DURATION);
                    if (pinNumber == LED_RED) {
                        LEDred.setImageResource(extraNumber == 0 ? R.drawable.red_off : R.drawable.red_on);
                    } else if (pinNumber == LED_GREEN) {
                        LEDgreen.setImageResource(extraNumber == 0 ? R.drawable.green_off : R.drawable.green_on);
                    } else if (pinNumber == LED_BLUE) {
                        LEDblue.setImageResource(extraNumber == 0 ? R.drawable.blue_off : R.drawable.blue_on);
                    } else if (pinNumber == ALARM) {
                        if (extraNumber == 0) {
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            mMediaPlayer = null;
                        } else {
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), mNotification);
                            mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();
                        }
                    } else if (pinNumber == NOTIFICATION) {
                        sendNotification(extraNumber);
                    } else if (pinNumber == TEXT) {
                        textView.setText(String.valueOf(extraNumber));
                    } else if (pinNumber == TOUCH_PAD_X_OUT) {
                        touchpadX = (int) Math.round(extraNumber * TOUCH_PAD_SIZE / 256.0);
                        positioner.setX(touchpadX - POSITIONER_SIZE / 2);
                    } else if (pinNumber == TOUCH_PAD_Y_OUT) {
                        touchpadY = (int) Math.round(extraNumber * TOUCH_PAD_SIZE / 256.0);
                        positioner.setY(touchpadX - POSITIONER_SIZE / 2);
                    }
                }
            });
        }
        return response;
    }

    private void sendNotification(final int extraNumber) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_settings_input_component_black_24dp)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("" + extraNumber);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private String getIpAddress() {
        String ipAddress = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ipAddress += getString(R.string.ip_address_prompt) + " "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ipAddress += getString(R.string.ip_address_fail) + " " + e.toString() + "\n";
        }
        if (ipAddress.equals("")) {
            ipAddress = getString(R.string.connect_internet);
        }
        return ipAddress;
    }
}