package develop.personal.aida.dmsassignmentthree;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class NFCActivity extends Activity implements NfcAdapter.OnNdefPushCompleteCallback,NfcAdapter.CreateNdefMessageCallback{
    //The array lists to hold our messages
    private ArrayList<String> contactToSendArray = new ArrayList<>();
    private ArrayList<String> contactReceivedArray = new ArrayList<>();

    private String receivedContactName;
    private String receivedContactPhone;
    String name ;
    String phone;

    //Text boxes to add and display our messages
    private EditText nameInput;
    private EditText phoneInput;
    private TextView nameDisplay;
    private TextView phoneDisplay;
    private NfcAdapter mNfcAdapter;

    public void update(View view) {
        if(contactToSendArray.size() > 0) {
            contactToSendArray = new ArrayList<>();
        }
        name = nameInput.getText().toString();
        phone = phoneInput.getText().toString();
        contactToSendArray.add(name);
        contactToSendArray.add(phone);


        nameInput.setText(null);
        phoneInput.setText(null);
        updateTextViews();
        Toast.makeText(this, "Contact Updated", Toast.LENGTH_LONG).show();
    }


    private  void updateTextViews() {

        //Populate Our list of messages we want to send
        if(contactToSendArray.size() > 0) {
            nameDisplay.append(contactToSendArray.get(0));
            phoneDisplay.append(contactToSendArray.get(1));

        }


        if (contactReceivedArray.size() > 0) {
            receivedContactName = contactReceivedArray.get(0);
            receivedContactPhone = contactReceivedArray.get(1);

        }
    }

    //Save our Array Lists of Messages for if the user navigates away
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nameDisplay", name);
        savedInstanceState.putString("phoneDisplay",phone);
    }

    //Load our Array Lists of Messages for when the user navigates back
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameDisplay.setText(savedInstanceState.getString("nameDisplay"));
        phoneDisplay.setText(savedInstanceState.getString("phoneDisplay"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        nameInput = (EditText) findViewById(R.id.nameInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        nameDisplay = (TextView) findViewById(R.id.nameDisplay);
        phoneDisplay = (TextView) findViewById(R.id.phoneDisplay);
        Button updateContact = (Button) findViewById(R.id.updateButton);

        updateContact.setText("UPDATE");
        updateTextViews();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null) {
            //This will refer back to createNdefMessage for what it will send
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            //This will be called if the message is sent successfully
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //This will be called when another NFC capable device is detected.
        if (contactToSendArray.size() == 0) {
            return null;
        }
        //We'll write the createRecords() method in just a moment
        NdefRecord[] recordsToAttach = createRecords();
        //When creating an NdefMessage we need to provide an NdefRecord[]
        return new NdefMessage(recordsToAttach);
    }


    @Override
    public void onNdefPushComplete(NfcEvent event) {
        contactToSendArray.clear();
    }
    public NdefRecord[] createRecords() {

        NdefRecord[] records = new NdefRecord[contactToSendArray.size() + 1];
        //To Create Messages Manually if API is less than
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (int i = 0; i < contactToSendArray.size(); i++){
                byte[] payload = contactToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));
                NdefRecord record = new NdefRecord(
                        NdefRecord.TNF_WELL_KNOWN,      //Our 3-bit Type name format
                        NdefRecord.RTD_TEXT,            //Description of our payload
                        new byte[0],                    //The optional id for our Record
                        payload);                       //Our payload for the Record

                records[i] = record;
            }
        }
        //Api is high enough that we can use createMime, which is preferred.
        else {
            for (int i = 0; i < contactToSendArray.size(); i++){
                byte[] payload = contactToSendArray.get(i).
                        getBytes(Charset.forName("UTF-8"));

                NdefRecord record = NdefRecord.createMime("text/plain",payload);
                records[i] = record;
            }
        }
        records[contactToSendArray.size()] =
                NdefRecord.createApplicationRecord(getPackageName());
        return records;
    }
    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(receivedArray != null) {
                contactReceivedArray.clear();
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) { continue; }
                    contactReceivedArray.add(string);
                }
                Toast.makeText(this, "Received " + contactReceivedArray.size() +
                        " Messages", Toast.LENGTH_LONG).show();
                updateTextViews();
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateTextViews();
        handleNfcIntent(getIntent());
    }
}