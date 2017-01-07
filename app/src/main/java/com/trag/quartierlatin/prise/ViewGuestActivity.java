package com.trag.quartierlatin.prise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.Guest;
import com.trag.quartierlatin.prise.extra.GuestAdapter;
import com.trag.quartierlatin.prise.extra.LogAdapter;
import com.trag.quartierlatin.prise.extra.PriseAppFactors;
import com.trag.quartierlatin.prise.extra.PriseEngine;
import com.trag.quartierlatin.prise.extra.PriseFilter;
import com.trag.quartierlatin.prise.extra.PriseWebAppFactors;
import com.trag.quartierlatin.prise.extra.StatusAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewGuestActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_fab_add)
    FloatingActionButton btnFabAdd;
    @BindView(R.id.btn_fab_sort)
    FloatingActionButton btnFabSort;
    @BindView(R.id.btn_fab_filter)
    FloatingActionButton btnFabFilter;
    @BindView(R.id.btn_fab_refresh)
    FloatingActionButton btnFabRefresh;
    @BindView(R.id.btn_fab_operation_root)
    FloatingActionMenu btnFabOperationRoot;
    @BindView(R.id.swrpv_guest_list)
    SwipeRefreshLayout swrpvGuestList;
    @BindView(R.id.txt_event_name)
    TextView txtEventName;
    @BindView(R.id.txt_event_description)
    TextView txtEventDescription;
    @BindView(R.id.linray_group_guestinfo)
    LinearLayout linrayGroupGuestinfo;
    @BindView(R.id.linray_group_eventtitle)
    LinearLayout linrayGroupEventtitle;

    private Context context;

    private ArrayAdapter<Guest> guestArrayAdapter;
    private Bundle bundle;
    @BindView(R.id.listw_guests)
    ListView listwGuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guest);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
//        listPosition = 0;
        doInOnCreate();

    }

    private void doInOnCreate() {
        context = ViewGuestActivity.this;
        txtEventName.setText(bundle.getString("eventName"));
        txtEventDescription.setText(bundle.getString("eventDescr"));

        // Store Event ID and Event Owner ID.
        PriseEngine.userId = bundle.getInt("userId");
        PriseEngine.eventId = bundle.getInt("eventId");

        swrpvGuestList.cancelLongPress();
        swrpvGuestList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swrpvGuestList.post(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });
        btnFabRefresh.setVisibility(View.GONE);
        linrayGroupEventtitle.setTag(linrayGroupGuestinfo.getVisibility());
        linrayGroupEventtitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int newVisibility = linrayGroupEventtitle.getVisibility();
                if (newVisibility != (int) linrayGroupEventtitle.getTag()) {
                    linrayGroupGuestinfo.animate()
                            .translationY(-linrayGroupGuestinfo.getTop())
                            .setDuration(2000);
                }
            }
        });

    }

    private Handler dataChangeHandler;
    private Runnable dataChangeRunnable;
    private ArrayList<Guest> guests;

    private void doGuestList() {
        if (ExtraUtils.isNetworkAvailable(ViewGuestActivity.this)) {
            guests = PriseEngine.getSortedGuestArrayList(this, bundle.getInt("userId"), bundle.getInt("eventId"), PriseEngine.sortById, PriseEngine.sortTypeId);
            guestArrayAdapter = new GuestAdapter(this, R.layout.guest_row, guests, bundle.getInt("userId"), bundle.getInt("eventId"));
            listwGuests.setAdapter(guestArrayAdapter);
//            setRefreshPosition();
            forgroundOperation();
            listwGuests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                    ImageView guestImg = null; // For binding lister in this class.
                    showGuestInfo(ViewGuestActivity.this, guests, position);


                }
            });
            listwGuests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int finalPosition = position;
//                    AlertDialog longItemsClick = new AlertDialog.Builder(ViewGuestActivity.this)
//                            .setItems(new String[]{ViewGuestActivity.this.getResources().getString(R.string.viewguest_dialog_item3_editinfo), ViewGuestActivity.this.getResources().getString(R.string.viewguest_dialog_item4_deleteguest)}, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which + 1) {
//                                        case 1:
//                                            if (ExtraUtils.isNetworkAvailable(ViewGuestActivity.this)) {
//                                                Intent editGuestIntent = new Intent(getApplicationContext(), EditGuestActivity.class);
//                                                editGuestIntent.putExtra("userId", guests.get(finalPosition).getUserId());
//                                                editGuestIntent.putExtra("eventId", guests.get(finalPosition).getEventId());
//                                                editGuestIntent.putExtra("gNo", guests.get(finalPosition).getGuestNo());
//                                                editGuestIntent.putExtra("gName", guests.get(finalPosition).getGuestName());
//                                                editGuestIntent.putExtra("gCorp", guests.get(finalPosition).getCorp());
//                                                editGuestIntent.putExtra("gPosition", guests.get(finalPosition).getPosition());
//                                                editGuestIntent.putExtra("seatRow", guests.get(finalPosition).getSeatRow());
//                                                editGuestIntent.putExtra("seatNo", guests.get(finalPosition).getSeatNo());
//                                                editGuestIntent.putExtra("award", guests.get(finalPosition).getAward());
//                                                editGuestIntent.putExtra("awardNo", guests.get(finalPosition).getAwardNo());
//                                                editGuestIntent.putExtra("gStatus", guests.get(finalPosition).getStatus());
//                                                startActivity(editGuestIntent);
//                                                break;
//                                            }
//                                            break;
//                                        case 2:
//                                            AlertDialog alrtDelete = new AlertDialog.Builder(ViewGuestActivity.this)
//                                                    .setMessage(ViewGuestActivity.this.getResources().getString(R.string.viewguest_alrt_longclick_wannadelete))
//                                                    .setNegativeButton(ViewGuestActivity.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            PriseEngine.deleteThisGuest(bundle.getInt("userId"), bundle.getInt("eventId"), guests.get(finalPosition).getGuestNo(), ViewGuestActivity.this);
//                                                            doGuestList();
//                                                        }
//                                                    })
//                                                    .setPositiveButton(ViewGuestActivity.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//
//                                                        }
//                                                    })
//                                                    .create();
//                                            alrtDelete.show();
//                                            break;
//                                        default: // ToDo Somethings
//                                    }
//                                }
//                            })
//                            .create();
//                    longItemsClick.show();
                    AlertDialog alrtLogAction = new AlertDialog.Builder(ViewGuestActivity.this)
                            .setItems(new CharSequence[]{
                                    getResources().getString(R.string.viewguest_dialog_logtype_11),
                                    getResources().getString(R.string.viewguest_dialog_logtype_13),
                                    getResources().getString(R.string.viewguest_dialog_logtype_12)
//                                    getResources().getString(R.string.viewguest_dialog_logtype_50),
//                                    getResources().getString(R.string.viewguest_dialog_logtype_51),
//                                    getResources().getString(R.string.viewguest_dialog_logtype_54)
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PriseEngine.saveLog(bundle.getInt("eventId"),
                                            bundle.getInt("userId"),
                                            bundle.getString("thisUserName"),
                                            guests.get(finalPosition).getGuestName(),
                                            i,
                                            guests.get(finalPosition).getGuestNo(),
                                            context);
                                    update();
                                }
                            })
                            .create();
                    alrtLogAction.show();
                    return true;
                }
            });
        }
        dataChangeHandler = new Handler();
        dataChangeRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    update();
                }
                dataChangeHandler.postDelayed(this, 15000);
            }
        };
        dataChangeRunnable.run();
        btnFabOperationRoot.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    btnFabOperationRoot.getMenuIconView().setImageResource(btnFabOperationRoot.isOpened() ? R.drawable.ic_autorenew_white_18dp : R.drawable.ic_reorder_white_18dp);
                } else {
                    update();
                    btnFabOperationRoot.close(false);
                    btnFabOperationRoot.getMenuIconView().setImageResource(btnFabOperationRoot.isOpened() ? R.drawable.ic_autorenew_white_18dp : R.drawable.ic_reorder_white_18dp);
                }
            }
        });
    }

    private void forgroundOperation() {
        listwGuests.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL | scrollState == SCROLL_STATE_FLING) {
                    if (btnFabOperationRoot.isOpened()) {

                        btnFabOperationRoot.close(false);
                    } else {
                        btnFabOperationRoot
                                .animate()
                                .alpha(0f);
                        linrayGroupEventtitle
                                .animate()
                                .translationY(-linrayGroupEventtitle.getHeight())
                                .alpha(0f)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        linrayGroupEventtitle
                                                .setVisibility(View.GONE);
                                    }
                                });

                    }
                } else {
//                    if (btnFabOperationRoot.isMenuHidden()) {
                    if (true) {
//                        btnFabOperationRoot.showMenu(false);
                        btnFabOperationRoot.setVisibility(View.VISIBLE);
                        btnFabOperationRoot
                                .animate()
                                .alpha(1.0f)
                                .setDuration(200);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

/*Do Overwrite*/

    @Override
    protected void onDestroy() {
        this.dataChangeHandler.removeCallbacks(this.dataChangeRunnable);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGuestList();
    }

    @Override
    public void onBackPressed() {
        if (btnFabOperationRoot.isOpened()) {
            btnFabOperationRoot.close(false);
        } else {
            super.onBackPressed();
        }
    }

    private int sortingType;

    @OnClick({R.id.btn_fab_add, R.id.btn_fab_sort, R.id.btn_fab_filter, R.id.btn_fab_refresh, R.id.btn_fab_operation_root, R.id.btn_fab_log})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fab_add:
                Intent insertGuestIntent = new Intent(ViewGuestActivity.this, InsertGuestActivity.class);
                insertGuestIntent.putExtra("userId", bundle.getInt("userId"));
                insertGuestIntent.putExtra("eventId", bundle.getInt("eventId"));
                startActivity(insertGuestIntent);
                btnFabOperationRoot.close(true);
                break;
            case R.id.btn_fab_sort:

                AlertDialog alrtSortOption = new AlertDialog.Builder(ViewGuestActivity.this)
                        .setTitle(getResources().getString(R.string.txt_sorting_sort_by))
                        .setItems(new CharSequence[]{
                                getResources().getString(R.string.txt_sorting_sort_by_guestno)
                                , getResources().getString(R.string.txt_sorting_sort_by_seat)
                                , getResources().getString(R.string.txt_sorting_sort_by_awardno)
                                , getResources().getString(R.string.txt_sorting_sort_by_award)
                                , getResources().getString(R.string.txt_sorting_sort_by_name)
                                , getResources().getString(R.string.txt_sorting_sort_by_corp)
                                , getResources().getString(R.string.txt_sorting_sort_by_position)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int sortById = which + 1;
                                AlertDialog alrtSortTypes = new AlertDialog.Builder(ViewGuestActivity.this)
                                        .setTitle(getResources().getString(R.string.txt_sorting_sort_type))
                                        .setItems(new CharSequence[]{
                                                getResources().getString(R.string.txt_sorting_sort_type_asc)
                                                , getResources().getString(R.string.txt_sorting_sort_type_desc)}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int sortTypeId = which + 1;
                                                PriseEngine.sortById = sortById;
                                                PriseEngine.sortTypeId = sortTypeId;
                                                update();
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .create();
                                alrtSortTypes.show();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alrtSortOption.show();
                this.guestArrayAdapter.notifyDataSetChanged();
                btnFabOperationRoot.close(true);
                break;
            case R.id.btn_fab_filter:
                AlertDialog alrtFilter = new AlertDialog.Builder(ViewGuestActivity.this)
                        .setItems(new CharSequence[]{
                                getResources().getString(R.string.txt_filter_by_status)
                                , getResources().getString(R.string.txt_filter_by_all)
//                                , getResources().getString(R.string.txt_sorting_sort_by_guestno)
//                                , getResources().getString(R.string.txt_sorting_sort_by_seat)
//                                , getResources().getString(R.string.txt_sorting_sort_by_awardno)
//                                , getResources().getString(R.string.txt_sorting_sort_by_award)
//                                , getResources().getString(R.string.txt_sorting_sort_by_name)
//                                , getResources().getString(R.string.txt_sorting_sort_by_corp)
//                                , getResources().getString(R.string.txt_sorting_sort_by_position)
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    AlertDialog alrtStatusFilter = new AlertDialog.Builder(ViewGuestActivity.this)
                                            .setAdapter(new StatusAdapter(ViewGuestActivity.this, R.layout.status_row, new Integer[]{1, 2, 3, 4, 5}), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PriseEngine.getFilterGuestsList(ViewGuestActivity.this, guestArrayAdapter, bundle.getInt("userId"), bundle.getInt("eventId"), guests, new PriseFilter(1, new int[]{which + 1}, null));
                                                }
                                            })
                                            .setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).create();
                                    alrtStatusFilter.show();
                                } else if (which == 1) {
                                    PriseEngine.priseFilter = null;
                                    update();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alrtFilter.show();
                btnFabOperationRoot.close(true);
                break;
            case R.id.btn_fab_refresh:
                update();
                btnFabOperationRoot.close(true);
                break;
            case R.id.btn_fab_log:
                Intent logIntext = new Intent(ViewGuestActivity.this, LogActivity.class);
                startActivity(logIntext);
                btnFabOperationRoot.close(true);
                break;
        }
    }

    public void update() {

        if (ExtraUtils.isNetworkAvailable(this)) {
            new Runnable() {
                @Override
                public void run() {
                    if (PriseEngine.priseFilter != null & guests != null) {
                        PriseEngine.getFilterGuestsList(ViewGuestActivity.this, guestArrayAdapter, bundle.getInt("userId"), bundle.getInt("eventId"), guests, PriseEngine.priseFilter);
                    } else {
                        PriseEngine.backupGuests = guests;
                        ViewGuestActivity.this.guests.clear();
                        ViewGuestActivity.this.guests.addAll(PriseEngine.getSortedGuestArrayList(ViewGuestActivity.this, bundle.getInt("userId"), bundle.getInt("eventId"), PriseEngine.sortById, PriseEngine.sortTypeId));
                        ViewGuestActivity.this.guestArrayAdapter.notifyDataSetChanged();
                    }
                }
            }.run();
            if (swrpvGuestList.isRefreshing()) {
                swrpvGuestList.setRefreshing(false);
            }
        }
    }

    private int tempPosition;
    private String tempGuestImgDir;
    private ImageView tempGuestImg;

    private Intent getIntent;
    private Intent intentPicker;
    private Intent intentTakeImage;
    private Intent intentChooser;


    public void showGuestInfo(final Context context, final List<Guest> guests, final int position) {
        final Dialog infoDialog = new Dialog(context);
        infoDialog.setContentView(R.layout.custom_guestinfo_view);
        ((TextView) infoDialog.findViewById(R.id.txt_name)).setText(guests.get(position).getGuestName());

        if (!guests.get(position).getCorp().equals(""))
            ((TextView) infoDialog.findViewById(R.id.txt_corp)).setText(guests.get(position).getCorp());
        else
            ((TextView) infoDialog.findViewById(R.id.txt_corp)).setText("-");

        if (!guests.get(position).getPosition().equals(""))
            ((TextView) infoDialog.findViewById(R.id.txt_position)).setText(guests.get(position).getPosition());
        else
            ((TextView) infoDialog.findViewById(R.id.txt_position)).setText("-");

        ((TextView) infoDialog.findViewById(R.id.txt_award)).setText(guests.get(position).getAward());

        ((TextView) infoDialog.findViewById(R.id.txt_awardno)).setText(String.valueOf(guests.get(position).getAwardNo()));

        ((TextView) infoDialog.findViewById(R.id.txt_seatno)).setText(String.valueOf(guests.get(position).getSeatNo()));

        ((TextView) infoDialog.findViewById(R.id.txt_seatrow)).setText(guests.get(position).getSeatRow());

        ((TextView) infoDialog.findViewById(R.id.txt_status)).setText(PriseEngine.switchStatus(guests.get(position).getStatus(), context));

        ((TextView) infoDialog.findViewById(R.id.txt_status)).setTextColor(ExtraUtils.colorSwitchStatus(guests.get(position).getStatus()));

        this.tempGuestImg = (ImageView) infoDialog.findViewById(R.id.img_guest);
        this.tempGuestImgDir = PriseWebAppFactors.URL_GUESTPIC_DIR + guests.get(position).getImgURI();
        Log.v("URL_GUESTPIC_DIR", (guests.get(position).getImgURI() == null ? "no_img.jpg" : tempGuestImgDir));
        Log.v("URL_GUESTPIC_DIR", guests.toString());
        Log.v("URL_GUESTPIC_DIR", tempGuestImgDir);
        if (guests.get(position).getImgURI() == null) {
            tempGuestImgDir = PriseWebAppFactors.URL_NO_GIMG;
        }
        Ion.with(context)
                .load(tempGuestImgDir)
                .withBitmap()
                .intoImageView(this.tempGuestImg);

        this.tempGuestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempPosition = position;

                getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                intentPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentPicker.setType("image/*");
                intentChooser = Intent.createChooser(getIntent, "Choose Image");
                intentChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentPicker});

                guestImgFile = new File(getExternalFilesDir("/")
                        , ("Ion_" + (new Random().nextInt(999) + 111) + ".jpg"));
                intentTakeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentTakeImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(guestImgFile));


                AlertDialog chooseDialog = new AlertDialog.Builder(context)
                        .setItems(new CharSequence[]{"Pick Image from Gallery", "Take a New One"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        startActivityForResult(intentChooser, PriseAppFactors.CHOOSE_IMAGE_REQ_CODE);
                                        break;
                                    case 1:
                                        if (intentTakeImage.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(intentTakeImage, PriseAppFactors.TAKE_PHOTO_REQ_CODE);
                                        }
                                        break;
                                }
                            }
                        })
                        .create();
                chooseDialog.show();

            }
        });

        ((LinearLayout) infoDialog.findViewById(R.id.linray_parentlinary)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            infoDialog.create();
        }
        infoDialog.show();

    }


    File guestImgFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap guestImgBitmap = null;
        FileOutputStream fos = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PriseAppFactors.TAKE_PHOTO_REQ_CODE:
                    Log.v("guestImgFile: ", guestImgFile.getAbsolutePath());
                    try {
                        guestImgBitmap = MediaStore.Images.Media.getBitmap(ViewGuestActivity.this.getContentResolver(),
                                Uri.fromFile(guestImgFile));
                    } catch (IOException e) { // Catch for cause of guestImgFile.
                        e.printStackTrace();
                    }
                    break;
                case PriseAppFactors.CHOOSE_IMAGE_REQ_CODE:
                    try {
                        guestImgBitmap = MediaStore.Images.Media.getBitmap(ViewGuestActivity.this.getContentResolver(),
                                data.getData()); //<-- Bug Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'android.net.Uri android.content.Intent.getData()' on a null object reference
// Catch for cause of data.getData().
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            try {
                Log.v("guestImgFile: ", guestImgFile.getAbsolutePath());
                fos = new FileOutputStream(guestImgFile);
                guestImgBitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
                fos.close();
                Ion.with(context)
                        .load(PriseWebAppFactors.URL_UPLOAD_IMG)
                        .setMultipartFile(PriseWebAppFactors.PARAM_GUEST_MULTIPART_IMG_NAME, guestImgFile)
                        .setMultipartParameter(PriseWebAppFactors.PARAM_GUEST_MULTIPART_IMG_PNAME, guests.get(tempPosition).getImgURI())
                        .setMultipartParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(PriseEngine.userId))
                        .setMultipartParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(guests.get(tempPosition).getEventId()))
                        .setMultipartParameter(PriseWebAppFactors.PARAM_GUEST_NUMBER, String.valueOf(guests.get(tempPosition).getGuestNo()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, final String result) {
                                if (e != null) {
                                    Log.v("result", result);
                                } else {
                                    tempGuestImgDir = PriseWebAppFactors.URL_GUESTPIC_DIR + result; //<-- result return nothing.
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.v("tempGuestImgDir.!trim()", tempGuestImgDir.length() + "");
                                            Log.v("tempGuestImgDir.trim()", tempGuestImgDir.trim().length() + "");
                                            Log.v("tempGuestImgDir.trim()", tempGuestImgDir.replace("\n", ""));
                                            Ion.with(context)
                                                    .load(tempGuestImgDir.replace("\n", ""))
                                                    .withBitmap()
                                                    .placeholder(R.drawable.no_img)
                                                    .intoImageView(tempGuestImg);
                                        }
                                    }).run();
                                }
                            }
                        });

            } catch (NullPointerException npex) {
                Toast.makeText(ViewGuestActivity.this, getResources().getString(R.string.viewguest_toast_no_img_picked), Toast.LENGTH_SHORT).show();
                npex.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
