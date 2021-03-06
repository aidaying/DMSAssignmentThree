package develop.personal.aida.dmsassignmentthree;

/**
 * Created by minju on 5/11/2016.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MessageDeliveredBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {  Toast toast;
        switch (getResultCode())
        {	 case Activity.RESULT_OK :
            toast = Toast.makeText(context, "SMS has been delivered",
                    Toast.LENGTH_SHORT);
            toast.show();
            break;
            case Activity.RESULT_CANCELED :
                toast = Toast.makeText(context,
                        "SMS has been cancelled before delivery",
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }
}
