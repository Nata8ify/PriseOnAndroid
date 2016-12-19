package com.trag.quartierlatin.prise.extra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.trag.quartierlatin.prise.R;

import java.util.ArrayList;

/**
 * Created by QuartierLatin on 18/6/2559.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private Context context;
    private int resource;
    private ArrayList<Event> events;
    private int currentUserId;


    public EventAdapter(Context context, int resource, ArrayList<Event> events, int userId) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
        this.currentUserId = userId;
    }


    private ListView sharedUsersList;
    private ArrayAdapter<String> usernameSharedList;
    private ArrayList<String> userList;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        row = layoutInflater.inflate(this.resource, parent, false);

        ImageView imgEventState = (ImageView) row.findViewById(R.id.img_eventstate);
        TextView txtEventName = (TextView) row.findViewById(R.id.txt_event_name);
        TextView txtEventDescription = (TextView) row.findViewById(R.id.txt_event_description);
        // Event Detial as Var
        final String eventName = this.events.get(position).getEvent();
        final int byUserId = this.events.get(position).getUserId();
        final int eventId = this.events.get(position).getEventId();
        txtEventName.setText(eventName);
        txtEventDescription.setText(this.events.get(position).getDescription());
        if (this.currentUserId != byUserId) {
            imgEventState.setImageResource(context.getResources().getIdentifier("ic_group_black_24dp", "drawable", getContext().getPackageName()));
        } else {
            imgEventState.setImageResource(context.getResources().getIdentifier("ic_person_black_24dp", "drawable", getContext().getPackageName()));
        }
        imgEventState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog sharingDialog = new Dialog(context);
                sharingDialog.setContentView(R.layout.sharing_dialog);

                TextView txtSharedEventTitle = (TextView) sharingDialog.findViewById(R.id.txtw_shr_event_title);

                TextView txtSharedEventHostUser = (TextView) sharingDialog.findViewById(R.id.txtw_shr_event_hostusername);
                if (currentUserId != byUserId)
                    txtSharedEventHostUser.setText(PriseEngine.getUsernameById(byUserId, context).trim());
                else
                    txtSharedEventHostUser.setText(context.getResources().getString(R.string.shre_event_by_you));

                txtSharedEventTitle.setText(eventName);
                final EditText edtxtUsername = (EditText) sharingDialog.findViewById(R.id.edtxt_insert_shr_user);
                Button btnCloseSharingDialog = (Button) sharingDialog.findViewById(R.id.btn_close_shr_event);
                Button btnSubmitSharingToUser = (Button) sharingDialog.findViewById(R.id.btn_submit_shr_user);

                sharedUsersList = (ListView) sharingDialog.findViewById(R.id.listw_shr_users);
                userList = PriseEngine.getSharedEventUsersList(eventId, byUserId, context);
                usernameSharedList = new ArrayAdapter<String>(context
                        , android.R.layout.simple_list_item_1
                        , userList);
                sharedUsersList.setAdapter(usernameSharedList);

                sharedUsersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        final int userPosition = position;
                        AlertDialog alrtDeleteShrToThisUser = new AlertDialog.Builder(context)
                                .setTitle(context.getResources().getString(R.string.shre_event_alert_deltitle))
                                .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.v("userList.get(i)",userPosition+"");
                                    }
                                })
                                .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(PriseEngine.deleteUserThisEvent(eventId, byUserId, String.valueOf(sharedUsersList.getItemAtPosition(userPosition)), context)) {
                                            Log.v("getItemAtPosition",String.valueOf(sharedUsersList.getItemAtPosition(userPosition)));
                                            doSharingNotifySetChanged(sharedUsersList, usernameSharedList, context, eventId, byUserId);
                                        } else {
                                            Log.v("getItemAtPosition",String.valueOf(sharedUsersList.getItemAtPosition(userPosition)));
                                        }
                                    }
                                })
                                .create();
                        alrtDeleteShrToThisUser.show();
                        return false;
                    }
                });


                sharingDialog.show();
                btnCloseSharingDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharingDialog.dismiss();
                    }
                });

                btnSubmitSharingToUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PriseEngine.insertUserToEvent(eventId, byUserId, edtxtUsername.getText().toString(), context)) {
                            doSharingNotifySetChanged(sharedUsersList, usernameSharedList, context, eventId, byUserId);
                            edtxtUsername.setText(String.valueOf(Character.MIN_VALUE));
                            Log.v("notifyDataSetChanged", "YEPP!");
                        } else {
                            Log.v("notifyDataSetChanged", "NOPEE!");
                        }
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sharingDialog.create();
                }
                Log.v("getSharedEventUsersList", PriseEngine.getSharedEventUsersList(eventId, byUserId, context).toString());


            }
        });

        return row;
    }

    public static void doSharingNotifySetChanged(ListView sharedUsersList, ArrayAdapter<String> usernameSharedList, Context context, int eventId, int byUserId){
        usernameSharedList = new ArrayAdapter<String>(context
                , android.R.layout.simple_list_item_1
                , PriseEngine.getSharedEventUsersList(eventId, byUserId, context));
        usernameSharedList.notifyDataSetChanged();
        sharedUsersList.setAdapter(usernameSharedList);
    }
}
