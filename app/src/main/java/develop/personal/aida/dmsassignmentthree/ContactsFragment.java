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
        rootView = inflater.inflate(R.layout.contacts_list_view, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


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


    private class ContactsAdapter extends ArrayAdapter<Contact> {

        private int resId;
        private ArrayList<Contact> contactlist;
        private LayoutInflater Inflater;
        private Context context;

        public ContactsAdapter(Context context, int textViewResourceId, List<Contact> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            resId = textViewResourceId;
            contactlist = (ArrayList<Contact>) objects;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if(v == null) {
                v = Inflater.inflate(resId, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
                holder.tv_phonenumber = (TextView) v.findViewById(R.id.tv_phonenumber);
                holder.iv_photoid = (ImageView) v.findViewById(R.id.iv_photo);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            Contact acontact = contactlist.get(position);

            if(acontact != null) {
                holder.tv_name.setText(acontact.getName());
                holder.tv_phonenumber.setText(acontact.getPhonenum());

                Bitmap bm = openPhoto(acontact.getPhotoid());

                if(bm != null) {
                    holder.iv_photoid.setImageBitmap(bm);
                } else {
                    holder.iv_photoid.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                }

            }

            return v;
        }

        private Bitmap openPhoto(long contactId) {
            Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    contactUri);

            if(input != null) {
                return BitmapFactory.decodeStream(input);
            }

            return null;
        }


        private class ViewHolder {
            ImageView iv_photoid;
            TextView tv_name;
            TextView tv_phonenumber;
        }

    }

}