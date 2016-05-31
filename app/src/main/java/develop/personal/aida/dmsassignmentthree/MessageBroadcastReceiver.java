package develop.personal.aida.dmsassignmentthree;
/**
 * Created by minju on 5/11/2016.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageBroadcastReceiver  extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {  // obtain the SMS message
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {  Object[] pdus = (Object[]) bundle.get("pdus");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < pdus.length; i++)
            {	SmsMessage message
                    = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String senderAddress
                        = message.getDisplayOriginatingAddress();
                String messageString = message.getDisplayMessageBody();
                stringBuilder.append("Received SMS:\n");
                stringBuilder.append("  Sender: ").append(senderAddress);
                stringBuilder.append(" Message: ").append(messageString);
                stringBuilder.append("\n");
            }
            Toast toast = Toast.makeText(context,
                    stringBuilder.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {	 Toast toast = Toast.makeText(context,
                "Error: no message data received", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}