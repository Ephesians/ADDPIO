package com.blahblah.addpio;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SensorStorage implements SensorEventListener {
    private static final String TYPE_PREFIX = "TYPE_";
    private static final String SENSOR_STRING_TYPE_PREFIX = "android.sensor.";
    private static final SensorStorage instance = new SensorStorage();
    public static SensorStorage getInstance() {
        return instance;
    }
    private SensorManager mSensorManager;
    private Map<Sensor, float[]> valueMap;
    private Context mContext;
    private SensorStorage() {
        super();
    }
    public void init(SensorManager sensorManager, Context context) {
        mSensorManager = sensorManager;
        mContext = context;
        Map<Integer, Sensor> sensorMap = new TreeMap<>();
        valueMap = new HashMap<>();

        TableLayout sensorTable = (TableLayout) ((Activity)mContext).findViewById(R.id.sensor_table);
        List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Map<Integer,String> sensorTypes = new HashMap();
        Field[] interfaceFields=Sensor.class.getFields();
        for(Field field:interfaceFields) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.startsWith(TYPE_PREFIX)) {
                try {
                    sensorTypes.put(field.getInt(null), name.substring(TYPE_PREFIX.length()));
                } catch (IllegalAccessException x) {

                }
            }
        }
        for (Sensor sensor: list){
            int type = sensor.getType();
            if (sensorMap.get(type) == null) {
                Sensor useSensor = sensorManager.getDefaultSensor(type);
                if (useSensor == null) {
                    useSensor = sensor;
                }
                sensorMap.put(type, useSensor);
                sensorManager.registerListener(this, useSensor, SensorManager.SENSOR_DELAY_NORMAL);

            }
        }
        for (Integer type : sensorMap.keySet()) {
            Sensor sensor = sensorMap.get(type);
            String typeName = sensorTypes.get(type);//defaultSensor. getStringType();
//                if (typeName.startsWith(SENSOR_STRING_TYPE_PREFIX)) {
//                    typeName = typeName.substring(SENSOR_STRING_TYPE_PREFIX.length());
//                }
            if (typeName == null) {
                typeName = "";
            }
            addRowToTable(sensorTable, typeName, String.valueOf(type), sensor.getName());
        }
    }
    private void addCellToTable(TableRow row, String contents) {
        TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        LinearLayout cell = new LinearLayout(mContext);
        cell.setBackgroundResource(R.drawable.cell_shape);
        cell.setLayoutParams(llp);
        TextView tv = new TextView(mContext);
        tv.setText(" " + contents + " ");
//        tv.setPadding(0, 0, 4, 3);
        cell.addView(tv);
        row.addView(cell);

    }
    private void addRowToTable(TableLayout table, String type, String number, String description) {
        TableRow row = new TableRow(mContext);
        LayoutParams rowParams = new TableRow.LayoutParams();
        rowParams.height = LayoutParams.WRAP_CONTENT;
        rowParams.width = LayoutParams.MATCH_PARENT;
        addCellToTable(row, number);
        addCellToTable(row, type);
        addCellToTable(row, description);
        table.addView(row, rowParams);
    }
    public String getSensorValue(int pinNumber, int index) {
        String response;
        Sensor sensor = mSensorManager.getDefaultSensor(pinNumber);
        if (sensor == null) {
            response = mContext.getString(R.string.bad_pin_number);
        } else {
            float[] values = valueMap.get(sensor);
            if (values == null) {
                response =  mContext.getString(R.string.no_sensor);
            } else {
                if (values.length <= index) {
                    response =  mContext.getString(R.string.bad_index);
                } else {
                    response = "" + values[index];
                }
            }
        }
        return response;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    public void onSensorChanged(SensorEvent event) {
        valueMap.put(event.sensor, event.values);
    }
}
