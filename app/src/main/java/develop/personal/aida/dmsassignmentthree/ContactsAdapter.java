package develop.personal.aida.dmsassignmentthree;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minju on 5/29/2016.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    private int resId;
    private ArrayList<Contact> contactlist;
    private LayoutInflater Inflater;
    private Activity context;

    public ContactsAdapter(Activity context, int textViewResourceId, List<Contact> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resId = textViewResourceId;
        contactlist = (ArrayList<Contact>) objects;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if(v == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.contacts_list_item, null);

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
                holder.iv_photoid.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
            }

        }

        return v;
    }

    private Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
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