package develop.personal.aida.dmsassignmentthree;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by minju on 5/15/2016.
 */

public class ContactsFragment extends Fragment {
    private View rootView;
    private ListView lv_contactlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.contacts_layout, container, false);

        lv_contactlist = (ListView) rootView.findViewById(R.id.contact_list);

        ContactsAdapter adapter = new ContactsAdapter(this.getActivity(), R.layout.contacts_list_item, getContactList());

        lv_contactlist.setAdapter(adapter);
        lv_contactlist
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> contactlist, View v,
                                            int position, long resid) {
                        Contact phonenumber = (Contact) contactlist
                                .getItemAtPosition(position);

                        if (phonenumber == null) {
                            return;
                        }

                        //Intent data = new Intent();

                    }
                });
        return rootView;
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
                String phonenumber = contactCursor.getString(1).replaceAll("-", "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                Contact acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));

                contactlist.add(acontact);

            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }



}