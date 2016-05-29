package develop.personal.aida.dmsassignmentthree;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button contactBtn;
    private FragmentManager manager;
    private FragmentTransaction trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // setting up fragment
            manager = getFragmentManager();
            trans = manager.beginTransaction();

        }


    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, SendSMS.class);
        startActivity(intent);
    }


}
