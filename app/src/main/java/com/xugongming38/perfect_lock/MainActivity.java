package com.xugongming38.perfect_lock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    float LightOld=0;
    boolean flag=true;

    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    private SensorManager sensorManager;
    /*
        private TextView lightLevel;
        private TextView AccLevel;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        //获取系统传感器的管理器
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //得到 光照传感器
        Sensor sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 光照传感器注册(register)监听,参数为(SensorEventListener实例,传感器对象,输出信息速率)
        sensorManager.registerListener(listenerLight, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        //同理。。
        sensorManager.registerListener(listenerAcc,sensorAcc,SensorManager.SENSOR_DELAY_NORMAL);


        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(this, LockReceiver.class);




        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lock();
            }
        });

    }
      void Lock(){

        if (policyManager.isAdminActive(componentName)) {
            policyManager.lockNow();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else{
            activeManager ();
        }

    }

    private void activeManager() {
        //使用隐式意图调用系统方法激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"一键锁屏");
        startActivity(intent);
    }

    private SensorEventListener listenerLight = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(flag){
                flag=false;
                LightOld=event.values[0];
                return;
            }
            float value = event.values[0];
            if(Math.abs(value-LightOld)>120) {
                Toast.makeText(MainActivity.this,""+Math.abs(value-LightOld),Toast.LENGTH_SHORT ).show();
                LightOld = value;

                Lock();
            }

            //lightLevel.setText("Current light level is " + value + " lx");

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    private SensorEventListener listenerAcc= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {              //传感器数据发生变化时
            float xValue = Math.abs(event.values[0]);
            float yValue = Math.abs(event.values[1]);
            float zValue = Math.abs(event.values[2]);
            if(xValue>15||yValue>15||zValue>50){
                //Toast.makeText(MainActivity.this, "摇一摇", Toast.LENGTH_SHORT).show();
                Lock();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (sensorManager != null) {
            //注销监听
            sensorManager.unregisterListener(listenerLight);
            sensorManager.unregisterListener(listenerAcc);
        }
    }

}
