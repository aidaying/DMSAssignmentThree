package develop.personal.aida.dmsassignmentthree;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by minju on 5/29/2016.
 */
public class ContactsDialog extends DialogFragment {


    private ListView dialogContactList;
    private OnMyDialogResult mDialogResult;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_contacts, null);
        dialogContactList = (ListView) DialogView.findViewById(R.id.dialogClistView);
        ContactsAdapter adapter = new ContactsAdapter(this.getActivity(), R.layout.contacts_list_item, getContactList());

        dialogContactList.setAdapter(adapter);
        dialogContactList
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> contactlist, View v,
                                            int position, long resid) {
                        Contact phonenumber = (Contact) contactlist
                                .getItemAtPosition(position);

                        String number = phonenumber.getPhonenum();
                        if (phonenumber == null) {
                            return;
                        }

                        if( mDialogResult != null ){
                            mDialogResult.finish(number);
                        }


                    }
                });

        builder.setTitle("Contacts Number");
        builder.setView(DialogView);
        return builder.create();

    }


    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    private ArrayList<Contact> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor contactCursor = getActivity().getContentResolver().query(uri, projection, null,
                selectionArgs, sortOrder);


        ArrayList<Contact> contactlist = new ArrayList<>();

        if(contactCursor.moveToFirst()) {
            do {
                String p = contactCursor.getString(1).replaceAll("-", "");
                String phone = p.replaceAll("\\(","");
                String phonenumber =phone.replaceAll("\\)","");


                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);

            } while (contactCursor.moveToNext());
        }

        return contactlist;


    }

    public interface OnMyDialogResult{
        void finish(String result);
    }
}




