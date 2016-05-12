package develop.personal.aida.dmsassignmentthree;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BluetoothActivity extends AppCompatActivity {

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            statusTextView.setText(R.string.status_unsupported);

        }
    }
}
