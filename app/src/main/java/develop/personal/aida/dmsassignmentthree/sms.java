package develop.personal.aida.dmsassignmentthree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by minju on 5/13/2016.
 */
public class sms extends Activity implements View.OnClickListener {


    private Button sendButton;
    private Button contactButton;
    private SMSSentBroadcastReceiver sentBroadcastReceiver;
    private SMSDeliveredBroadcastReceiver deliveredBroadcastReceiver;
    private final String SMS_SENT_ACTION = "SMS_SENT";
    private final String SMS_DELIVERED_ACTION = "SMS_DELIVERED";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        contactButton = (Button) findViewById(R.id.contact_button);
        contactButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
