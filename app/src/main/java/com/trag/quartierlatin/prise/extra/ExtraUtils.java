package com.trag.quartierlatin.prise.extra;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trag.quartierlatin.prise.R;

import java.util.List;

/**
 * Created by QuartierLatin on 21/6/2559.
 */
public class ExtraUtils {

    public static boolean isNetworkAvailable(Context context) {
        boolean isAvailable = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        if (!isAvailable) {
//            final AlertDialog noConnectionDialog = new AlertDialog.Builder(context)
//                    .setPositiveButton(context.getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .setMessage(context.getResources().getString(R.string.extra_no_connection)).create();
//            noConnectionDialog.show();
            Toast.makeText(context, context.getResources().getString(R.string.viewguest_txt_no_connection), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(context, context.getResources().getString(R.string.viewguest_txt_got_connection), Toast.LENGTH_SHORT).show();
        }
        return isAvailable;
    }

    public static void portableProgressDialog(Context context) {
        try {
            ProgressDialog loginProgressDialog = ProgressDialog.show(context, ""
                    , context.getResources().getString(R.string.progress)
                    , true);
            loginProgressDialog.show();
        } catch (Exception e) {

        }
    }

    public static ProgressDialog simpleProgressDialog(Context context) {
        return ProgressDialog.show(context, ""
                , context.getResources().getString(R.string.progress)
                , false
                , true);
    }

    public static void showGuestInfo(Context context, List<Guest> guests, int position) {
//        final Dialog infoDialog = new Dialog(context);
//        infoDialog.setContentView(R.layout.custom_guestinfo_view);

        final AlertDialog.Builder infoDialogBuilder = new AlertDialog.Builder(context);
        View infoView = (LayoutInflater.from(context).inflate(R.layout.custom_guestinfo_view, null));
        infoDialogBuilder.setView(infoView);

        ((TextView)infoView.findViewById(R.id.txt_name)).setText(guests.get(position).getGuestName());

        if(!guests.get(position).getCorp().equals(""))
            ((TextView)infoView.findViewById(R.id.txt_corp)).setText(guests.get(position).getCorp());
        else
            ((TextView)infoView.findViewById(R.id.txt_corp)).setText("-");

        if(!guests.get(position).getPosition().equals(""))
            ((TextView)infoView.findViewById(R.id.txt_position)).setText(guests.get(position).getPosition());
        else
            ((TextView)infoView.findViewById(R.id.txt_position)).setText("-");

        ((TextView)infoView.findViewById(R.id.txt_award)).setText(guests.get(position).getAward());

        ((TextView)infoView.findViewById(R.id.txt_awardno)).setText(String.valueOf(guests.get(position).getAwardNo()));

        ((TextView)infoView.findViewById(R.id.txt_seatno)).setText(String.valueOf(guests.get(position).getSeatNo()));

        ((TextView)infoView.findViewById(R.id.txt_seatrow)).setText(guests.get(position).getSeatRow());

        ((TextView)infoView.findViewById(R.id.txt_status)).setText(PriseEngine.switchStatus(guests.get(position).getStatus(), context));
        ((TextView)infoView.findViewById(R.id.txt_status)).setTextColor(colorSwitchStatus(guests.get(position).getStatus()));
        final AlertDialog infoAlrt = infoDialogBuilder.create();
        ((LinearLayout)infoView.findViewById(R.id.linray_parentlinary)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoAlrt.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            infoDialogBuilder.create();
        }
        infoAlrt.show();

    }

    private static int colorSwitchStatus(int statusId){
        switch (statusId){
            case 1 : return Color.rgb(0, 136, 0);
            case 2 : return Color.GRAY;
            case 3 : return Color.RED;
            case 4 : return Color.BLUE;
            case 5 : return Color.rgb(247, 149, 42);
        }
        return 5;
    }
}
