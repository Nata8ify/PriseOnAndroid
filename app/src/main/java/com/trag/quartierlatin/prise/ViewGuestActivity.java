package com.trag.quartierlatin.prise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.trag.quartierlatin.prise.extra.ExtraUtils;
import com.trag.quartierlatin.prise.extra.Guest;
import com.trag.quartierlatin.prise.extra.GuestAdapter;
import com.trag.quartierlatin.prise.extra.LogAdapter;
import com.trag.quartierlatin.prise.extra.PriseEngine;
import com.trag.quartierlatin.prise.extra.PriseFilter;
import com.trag.quartierlatin.prise.extra.StatusAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewGuestActivity extends AppCompatActivity {

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
                    ExtraUtils.showGuestInfo(ViewGuestActivity.this, guests, position);
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
            case R.id.btn_fab_log :
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


}
