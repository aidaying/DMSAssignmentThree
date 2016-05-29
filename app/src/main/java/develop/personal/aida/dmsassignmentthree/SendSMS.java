package develop.personal.aida.dmsassignmentthree;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by minju on 5/13/2016.
 */
public class SendSMS extends Activity implements View.OnClickListener {


    public static final String SELECTED_PHONE = "selectedphone";
    public static final int SUCCESS = 1;
    private Button sendButton;
    private Button contactButton;
    private Button nfcButton;
    private MessageSentBroadcastReceiver sentBroadcastReceiver;
    private MessageDeliveredBroadcastReceiver deliveredBroadcastReceiver;
    private final String SMS_SENT_ACTION = "SMS_SENT";
    private final String SMS_DELIVERED_ACTION = "SMS_DELIVERED";
    private EditText pnumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        contactButton = (Button) findViewById(R.id.contact_button);
        contactButton.setOnClickListener(this);
        nfcButton = (Button) findViewById(R.id.nfc_button);
        nfcButton.setOnClickListener(this);

        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContactsDialog contactsDialog = new ContactsDialog();
                FragmentManager fm = getFragmentManager();
                contactsDialog.show(fm, "");
                contactsDialog.setDialogResult(new ContactsDialog.OnMyDialogResult(){
                    public void finish(String result){
                        pnumber = (EditText) findViewById(R.id.number_text);
                        pnumber.setText(result);
                    }
                });

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // create send broadcast receiver and register it
        sentBroadcastReceiver = new MessageSentBroadcastReceiver();
        registerReceiver(sentBroadcastReceiver, new IntentFilter(SMS_SENT_ACTION));
        // create delivered broadcast receiver and register it
        deliveredBroadcastReceiver = new MessageDeliveredBroadcastReceiver();
        registerReceiver(deliveredBroadcastReceiver, new IntentFilter(SMS_DELIVERED_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();

        // if sentBroadcast receiver is not null then unregister the receiver
        if(sentBroadcastReceiver != null) {
            unregisterReceiver(sentBroadcastReceiver);
        }

        if(deliveredBroadcastReceiver != null) {
            unregisterReceiver(deliveredBroadcastReceiver);
        }
    }

    // implementation of OnClickListener method
    public void onClick(View view) {

        // if user clicks send button
        if(view == sendButton) {
            // get phone number and message from textview in acitivity_sms.xml
            TextView numberTextView = (TextView) findViewById(R.id.number_text);
            String numberString = numberTextView.getText().toString();
            TextView messageTextView = (TextView) findViewById(R.id.message_text);
            String messageString = messageTextView.getText().toString();

            // send the sms message
            PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT_ACTION), 0);
            PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED_ACTION), 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numberString, null, messageString, sentPendingIntent, deliveredPendingIntent);
        }




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            if (resultCode == SUCCESS) {
                ((TextView)findViewById(R.id.number_text)).setText(data.getStringExtra(SELECTED_PHONE));
            } else {
                ((TextView)findViewById(R.id.number_text)).setText("");
            }
        }
    }


}
