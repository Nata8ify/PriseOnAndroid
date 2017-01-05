package com.trag.quartierlatin.prise.extra;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
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

    public static void showGuestInfo(final Context context, List<Guest> guests, int position, ImageView guestImg) {
        final Dialog infoDialog = new Dialog(context);
        infoDialog.setContentView(R.layout.custom_guestinfo_view);
        ((TextView)infoDialog.findViewById(R.id.txt_name)).setText(guests.get(position).getGuestName());

        if(!guests.get(position).getCorp().equals(""))
            ((TextView)infoDialog.findViewById(R.id.txt_corp)).setText(guests.get(position).getCorp());
        else
            ((TextView)infoDialog.findViewById(R.id.txt_corp)).setText("-");

        if(!guests.get(position).getPosition().equals(""))
            ((TextView)infoDialog.findViewById(R.id.txt_position)).setText(guests.get(position).getPosition());
        else
            ((TextView)infoDialog.findViewById(R.id.txt_position)).setText("-");

        ((TextView)infoDialog.findViewById(R.id.txt_award)).setText(guests.get(position).getAward());

        ((TextView)infoDialog.findViewById(R.id.txt_awardno)).setText(String.valueOf(guests.get(position).getAwardNo()));

        ((TextView)infoDialog.findViewById(R.id.txt_seatno)).setText(String.valueOf(guests.get(position).getSeatNo()));

        ((TextView)infoDialog.findViewById(R.id.txt_seatrow)).setText(guests.get(position).getSeatRow());

        ((TextView)infoDialog.findViewById(R.id.txt_status)).setText(PriseEngine.switchStatus(guests.get(position).getStatus(), context));

        ((TextView)infoDialog.findViewById(R.id.txt_status)).setTextColor(colorSwitchStatus(guests.get(position).getStatus()));

        guestImg = (ImageView) infoDialog.findViewById(R.id.img_guest);
        String guestImgDir = PriseWebAppFactors.URL_GUESTPIC_DIR + guests.get(position).getImgURI();
        Log.v("URL_GUESTPIC_DIR", (guests.get(position).getImgURI()==null?"no_img.jpg":guestImgDir));
        Log.v("URL_GUESTPIC_DIR", guests.toString());
        Log.v("URL_GUESTPIC_DIR", guestImgDir);
        if(guests.get(position).getImgURI()==null){
            guestImgDir = PriseWebAppFactors.URL_NO_GIMG;
        }
        Ion.with(context)
                .load(guestImgDir)
                .withBitmap()
                .intoImageView(guestImg);


        ((LinearLayout)infoDialog.findViewById(R.id.linray_parentlinary)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            infoDialog.create();
        }
        infoDialog.show();

//        AlertDialog alrtInfo = new AlertDialog.Builder(context)
//                .setMessage(
//                        "\n" + context.getResources().getString(R.string.viewguest_info_seatrow)
//                                + " " + guests.get(position).getSeatRow() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_seatno)
//                                + " " + guests.get(position).getSeatNo() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_guestname)+"\n"
//                                + "" + guests.get(position).getGuestName() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_corp)+"\n"
//                                + "" + guests.get(position).getCorp() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_position)+"\n"
//                                + "" + guests.get(position).getPosition() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_award)
//                                + " " + guests.get(position).getAward() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_awardno)
//                                + " " + guests.get(position).getAwardNo() + "\n" +
//                                "\n" + context.getResources().getString(R.string.viewguest_info_status)
//                                + " " + PriseEngine.switchStatus(guests.get(position).getStatus(), context) + "\n")
//                .setPositiveButton(context.getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create();
//        alrtInfo.show();
    }

    public static int colorSwitchStatus(int statusId){
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
