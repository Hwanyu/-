package com.example.report_1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    TextView text;
    Button bt_connect, bt_disconnect;
    Button dc_cw, dc_stop ,dc_ccw;
    SeekBar cw_speed, ccw_speed;
    TextView cw_value, ccw_value;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;
    Handler mBluetoothHandler;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
        bt_connect = (Button) findViewById(R.id.bt_connect);
        bt_disconnect = (Button) findViewById(R.id.bt_disconnect);

        dc_cw = (Button) findViewById(R.id.dc_cw);
        dc_stop = (Button) findViewById(R.id.dc_stop);
        dc_ccw = (Button) findViewById(R.id.dc_ccw);

        cw_speed = (SeekBar) findViewById(R.id.cw_speed);
        ccw_speed = (SeekBar) findViewById(R.id.ccw_speed);
        cw_value = (TextView) findViewById(R.id.cw_value);
        ccw_value = (TextView) findViewById(R.id.ccw_value);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //event

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT


                    },
                    1);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH

                    },
                    1);
        }

        //블루투스 연결
        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listPairedDevices();

                bt_connect.setBackgroundColor(Color.GREEN);
                bt_disconnect.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_cw.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_stop.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_ccw.setBackgroundColor(Color.parseColor("#CCCCCC"));
            }
        });

        //블루투스 해제
        bt_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_connect.setBackgroundColor(Color.parseColor("#CCCCCC"));
                bt_disconnect.setBackgroundColor(Color.parseColor("#FF0000"));
                dc_cw.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_stop.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_ccw.setBackgroundColor(Color.parseColor("#CCCCCC"));

                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.cancel();
                    bt_connect.setEnabled(true);
                    bt_disconnect.setEnabled(false);

                    dc_cw.setClickable(false);
                    dc_stop.setClickable(false);
                    dc_ccw.setClickable(false);

                    cw_value.setText(String.valueOf(0));
                    cw_speed.setProgress(0);
                    ccw_value.setText(String.valueOf(0));
                    ccw_speed.setProgress(0);

                    mThreadConnectedBluetooth = null;
                    text.setText("Connection State(연결X)");
                    text.setBackgroundColor(Color.parseColor("#FF0000"));

                }
            }
        });


        //송신단
        //DC_CW
        dc_cw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dc_cw.setBackgroundColor(Color.GREEN);
                dc_stop.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_ccw.setBackgroundColor(Color.parseColor("#CCCCCC"));

                cw_speed.setVisibility(View.VISIBLE);
                cw_speed.setEnabled(true);
                ccw_speed.setVisibility(View.VISIBLE);
                ccw_speed.setEnabled(false);
                cw_value.setText(String.valueOf(0));
                cw_speed.setProgress(0);
                ccw_value.setText(String.valueOf(0));
                ccw_speed.setProgress(0);
                if(mThreadConnectedBluetooth!=null) {
                    mThreadConnectedBluetooth.write("C_1\n");
                }
            }
        });

        //DC_STOP
        dc_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dc_stop.setBackgroundColor(Color.RED);
                dc_cw.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_ccw.setBackgroundColor(Color.parseColor("#CCCCCC"));

                cw_speed.setVisibility(View.VISIBLE);
                cw_speed.setEnabled(false);
                ccw_speed.setVisibility(View.VISIBLE);
                ccw_speed.setEnabled(false);
                cw_value.setText(String.valueOf(0));
                cw_speed.setProgress(0);
                ccw_value.setText(String.valueOf(0));
                ccw_speed.setProgress(0);
                if(mThreadConnectedBluetooth!=null) {
                    mThreadConnectedBluetooth.write("S_1\n");
                }
            }
        });

        //DC_CCW
        dc_ccw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dc_ccw.setBackgroundColor(Color.YELLOW);
                dc_cw.setBackgroundColor(Color.parseColor("#CCCCCC"));
                dc_stop.setBackgroundColor(Color.parseColor("#CCCCCC"));

                cw_speed.setVisibility(View.VISIBLE);
                cw_speed.setEnabled(false);
                ccw_speed.setVisibility(View.VISIBLE);
                ccw_speed.setEnabled(true);
                cw_value.setText(String.valueOf(0));
                cw_speed.setProgress(0);
                ccw_value.setText(String.valueOf(0));
                ccw_speed.setProgress(0);
                if(mThreadConnectedBluetooth!=null) {
                    mThreadConnectedBluetooth.write("W_1\n");
                }
            }
        });
        cw_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cw_value.setText(String.valueOf(cw_speed.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mThreadConnectedBluetooth!=null){
                    mThreadConnectedBluetooth.write(String.format("A_%d\n", cw_speed.getProgress()));
                }
            }
        });
        ccw_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ccw_value.setText(String.valueOf(ccw_speed.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mThreadConnectedBluetooth!=null){
                    mThreadConnectedBluetooth.write(String.format("B_%d\n", ccw_speed.getProgress()));
                }
            }
        });


        //수신단
        mBluetoothHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }
        });
    }


    // 이하 블루투스 관련 함수
    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());

                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    void connectSelectedDevice(String selectedDeviceName) {
        for (BluetoothDevice tempDevice : mPairedDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
            bt_connect.setEnabled(false);
            bt_disconnect.setEnabled(true);
            dc_cw.setClickable(true);
            dc_stop.setClickable(true);
            dc_ccw.setClickable(true);
            text.setText("Connection State(연결O)");
            text.setBackgroundColor(Color.parseColor("#00FF00"));

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }
    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
