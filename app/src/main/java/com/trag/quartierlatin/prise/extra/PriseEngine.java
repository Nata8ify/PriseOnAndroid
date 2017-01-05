package com.trag.quartierlatin.prise.extra;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.trag.quartierlatin.prise.R;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Created by QuartierLatin on 15/6/2559.
 */
public class PriseEngine {
    public static int sortById = 1;// 1.guestNo 2.seat 3.awardNo 4.award 5.name 6.corp 7.position
    public static int sortTypeId = 1;// 1.Ascending 2.Descending
    public static int filterStatusId = 1;
    public static final String SORTING_ASC = "asc";// Ascending
    public static final String SORTING_DESC = "desc";// Descending
    public static PriseFilter priseFilter = null;
    public static int userId = 0;
    public static int eventId = 0;
    public static ArrayList<Guest> backupGuests;

    // Guest Handler
    public static ArrayList<Guest> getGuestArrayList(Context context, int userId, int eventId) {
        ArrayList<Guest> guestArrayList = null;
        JsonObject gJSON = null;
        try {
            gJSON = Ion.with(context)
                    .load(PriseWebAppFactors.URL_GUEST_LIST_VIA_JSON)
                    .setTimeout(3500)
//                     .setBodyParameter("opt",PriseWebAppFactors.OPT_VIEW_ALL_GUESTS)
                    .setBodyParameter("userid", userId + "")
                    .setBodyParameter("eventid", eventId + "")
                    .asJsonObject()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //Do Timeout Handler
            e.printStackTrace();
//            AlertDialog alrtOut =  new AlertDialog.Builder(context)
//                    .setMessage("ISSUE 001 : CONNECTION TIME OUT")
//                    .setNegativeButton(context.getResources().getString(R.string.acknowledge), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .create();
//            alrtOut.show();

            return parseJSONToArrayList(new JsonObject(), guestArrayList);
        }
        return parseJSONToArrayList(gJSON, guestArrayList);
    }

    public static ArrayList<Guest> getSortedGuestArrayList(Context context, int userId, int eventId, int sortById, int sortTypeId) {
        String sortBy = null;
        switch (sortById) {
//            case 0: sortBy = "";
//                break;
            case 1:
                sortBy = "ginfo_no";
                break;
            case 2:
                sortBy = "seat";
                break;
            case 3:
                sortBy = "ginfo_awardno";
                break;
            case 4:
                sortBy = "ginfo_award";
                break;
            case 5:
                sortBy = "ginfo_guestname";
                break;
            case 6:
                sortBy = "ginfo_corp";
                break;
            case 7:
                sortBy = "ginfo_position";
                break;
            default: //TODO
                // 1.guestNo 2.seat 3.awardNo 4.award 5.name 6.corp 7.position
        }
        ArrayList<Guest> guestArrayList = null;
        JsonObject gJSON = null;
        try {
            gJSON = Ion.with(context)
                    .load(PriseWebAppFactors.URL_GUEST_LIST_VIA_JSON)
                    .setTimeout(3500)
//                     .setBodyParameter("opt",PriseWebAppFactors.OPT_VIEW_ALL_GUESTS)
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, userId + "")
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, eventId + "")
                    .setBodyParameter("sortby", sortBy)
                    .setBodyParameter("sorttype", sortTypeId == 1 ? "asc" : "desc")
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_FILTER_NUMBER, "")
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_FILTER_ARGUMENT, "")
                    .asJsonObject()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //Do Timeout Handler
            e.printStackTrace();
            return new ArrayList<Guest>();
        }
        return parseJSONToArrayList(gJSON, guestArrayList);
    }

    public static ArrayList<Guest> getSortedWithFilterGuestArrayList(Context context, int userId, int eventId, int sortById, int sortTypeId) {
        String sortBy = null;
        switch (sortById) {
//            case 0: sortBy = "";
//                break;
            case 1:
                sortBy = "ginfo_no";
                break;
            case 2:
                sortBy = "seat";
                break;
            case 3:
                sortBy = "ginfo_awardno";
                break;
            case 4:
                sortBy = "ginfo_award";
                break;
            case 5:
                sortBy = "ginfo_guestname";
                break;
            case 6:
                sortBy = "ginfo_corp";
                break;
            case 7:
                sortBy = "ginfo_position";
                break;
            default: //TODO
                // 1.guestNo 2.seat 3.awardNo 4.award 5.name 6.corp 7.position
        }
        ArrayList<Guest> guestArrayList = null;
        JsonObject gJSON = null;
        try {
            gJSON = Ion.with(context)
                    .load(PriseWebAppFactors.URL_GUEST_LIST_VIA_JSON)
                    .setTimeout(3500)
//                     .setBodyParameter("opt",PriseWebAppFactors.OPT_VIEW_ALL_GUESTS)
                    .setBodyParameter("userid", userId + "")
                    .setBodyParameter("eventid", eventId + "")
                    .setBodyParameter("sortby", sortBy)
                    .setBodyParameter("sorttype", sortTypeId == 1 ? "asc" : "desc")
                    .asJsonObject()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //Do Timeout Handler
            e.printStackTrace();
            return parseJSONToArrayList(new JsonObject(), guestArrayList);
        }
        return parseJSONToArrayList(gJSON, guestArrayList);
    }

    private static ArrayList<Guest> parseJSONToArrayList(JsonObject jsonObject, ArrayList<Guest> guestArrayList) {
        try {
            guestArrayList = new ArrayList<Guest>();
            Guest guest;
            JsonArray guests = jsonObject.getAsJsonArray("guests");

            Log.v("guestsguests: ", guests.toString());
            for (JsonElement jsonElement : guests) {
                guest = new Guest();
                guest.setGuestNo(jsonElement.getAsJsonObject().get("guestNo").getAsInt());
                guest.setSeatRow(jsonElement.getAsJsonObject().get("seatRow").getAsString());
                guest.setSeatNo(jsonElement.getAsJsonObject().get("seatNo").getAsString());
                guest.setAwardNo(jsonElement.getAsJsonObject().get("awardNo").getAsInt());
                guest.setAward(jsonElement.getAsJsonObject().get("award").getAsString());
                guest.setGuestName(jsonElement.getAsJsonObject().get("guestName").getAsString());
                guest.setCorp(jsonElement.getAsJsonObject().get("corp").getAsString());
                guest.setPosition(jsonElement.getAsJsonObject().get("position").getAsString());
                guest.setStatus(jsonElement.getAsJsonObject().get("status").getAsInt());
                guest.setEventId(jsonElement.getAsJsonObject().get("eventId").getAsInt());
                guest.setUserId(jsonElement.getAsJsonObject().get("userId").getAsInt());
//                Log.v("imgURI",jsonElement.toString());
                if (jsonElement.toString().contains("imgURI"))
                    guest.setImgURI(jsonElement.getAsJsonObject().get("imgURI").getAsString());
//            Log.v("guestName",guest.toString());
                guestArrayList.add(guest);
            }
            return guestArrayList;
        } catch (NullPointerException nex) {
            return backupGuests;
        }
    }

    public static void editGuestInformation(String seatRow, String seatNo, String guestName, String corp, String position, String award, int awardNo, int status, int userId, int eventId, int gNo, Context context) {
        try {
            String callBack = Ion.with(context)
                    .load(PriseWebAppFactors.URL_EDIT_GUEST_VIA_JSON)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SEAT_ROW, seatRow)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SEAT_NO, seatNo)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_NAME, guestName)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_CORP, corp)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_POSITION, position)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_AWARD, award)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_AWARWD_NUMBER, String.valueOf(awardNo))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_STATUS, String.valueOf(status))
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_NUMBER, String.valueOf(gNo))
                    .asString()
                    .get();
            Log.v("callBack", callBack);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void insertGuestInformation(String seatRow, String seatNo, String guestName, String corp, String position, String award, int awardNo, int status, int userId, int eventId, Context context) {
        try {
            String callBack = Ion.with(context) // <-- Change to Multipart Request
                    .load(PriseWebAppFactors.URL_INSERT_GUEST)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SEAT_ROW, seatRow)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SEAT_NO, String.valueOf(seatNo))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_NAME, guestName)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_CORP, corp)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_POSITION, position)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_AWARD, award)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_AWARWD_NUMBER, String.valueOf(awardNo))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_STATUS_INSERT, String.valueOf(status))
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId))
                    .asString()
                    .get();
//            Log.v("callBack",callBack+"");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static String switchStatus(int status, Context context) {
        switch (status) {
            case 1:
                return context.getResources().getString(R.string.viewguest_status_ready);
            case 2:
                return context.getResources().getString(R.string.viewguest_status_unready);
            case 3:
                return context.getResources().getString(R.string.viewguest_status_absent);
            case 4:
                return context.getResources().getString(R.string.viewguest_status_received);
            case 5:
                return context.getResources().getString(R.string.viewguest_status_quited);
            default:
                return null;
        }
    }

    // Event Handler


    public static ArrayList<Event> getEvents(Context appContext, int userId) {
        ArrayList<Event> events = null;
        try {
            Log.v("userId", userId + "");
            JsonArray eventJson = Ion.with(appContext)
//                    .load(PriseWebAppFactors.URL_EVENT_LIST_VIA_JSON)
                    .load(PriseWebAppFactors.URL_SHARED_EVENT_LIST_VIA_JSON)
                    .setBodyParameter("userid", userId + "")
                    .asJsonObject()
                    .get()
                    .getAsJsonArray("events");
            Gson gson = new Gson();
            Type eventTypeCollection = new TypeToken<ArrayList<Event>>() {
            }.getType();
            events = gson.fromJson(eventJson, eventTypeCollection);
//            Log.v("events", events.toString());
            return events;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void insertEvent(int userId, String eventName, String eventDesc, Context context) {
        try {
            Ion.with(context)
                    .load(PriseWebAppFactors.URL_INSERT_EVENT)
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_TITLE, eventName)
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_DESC, eventDesc)
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .asString()
                    .withResponse()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void deleteEvent(int userId, int eventId, Context context) {
        try {
            Ion.with(context)
                    .load(PriseWebAppFactors.URL_DELETE_EVENT)
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .asString()
                    .withResponse()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static boolean editGuestStatus(int status, int guestNo, int userId, int eventId, Context context) {
        try {
            Ion.with(context)
                    .load(PriseWebAppFactors.URL_EDIT_GUEST_STATUS_VIA_JSON)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_STATUS, String.valueOf(status))
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_NUMBER, String.valueOf(guestNo))
                    .asString()
                    .get();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteThisGuest(int userId1, int eventId1, int guestNo, Context context) {
        try {
            Ion.with(context)
                    .load(PriseWebAppFactors.URL_DELETE_GUEST)
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId1))
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId1))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_NUMBER, String.valueOf(guestNo))
                    .asString()
                    .withResponse()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Guest> toGuestArrayList(String guestsString) {
        ArrayList<Guest> guests = null;
        Guest guest = null;
        JsonObject guestJSONObj = new JsonObject();
        return null;
    }

    public static void getFilterGuestsList(Context viewGuestActivity, ArrayAdapter<Guest> guestArrayAdapter, int userId, int eventId, ArrayList<Guest> guests, PriseFilter priseFilter) {
        try {
            Log.v("sortProperties", PriseEngine.sortById + " | " + PriseEngine.sortTypeId);
            JsonArray guestJSONArray = Ion.with(viewGuestActivity)
                    .load(PriseWebAppFactors.URL_GUEST_LIST_VIA_JSON)
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_FILTER_NUMBER, String.valueOf(priseFilter.getFilterType()))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_FILTER_ARGUMENT, String.valueOf(priseFilter.getFilterNumericArguments()[0]))
                    .setBodyParameter(PriseWebAppFactors.PARAM_USER_ID, String.valueOf(userId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_EVENT_ID, String.valueOf(eventId))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SORT_BY_ID, String.valueOf(PriseEngine.sortById))
                    .setBodyParameter(PriseWebAppFactors.PARAM_GUEST_SORT_TYPE_ID, sortTypeId == 1 ? "asc" : "desc")
                    .asJsonObject()
                    .get() // <<<<<<<<<<<<<<<
                    .getAsJsonArray("guests");
//            Log.v("guestJSONObj", guestJSONArray.toString());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Guest>>() {
            }.getType();
            guests.clear();
            ArrayList<Guest> newGuests = (ArrayList<Guest>) gson.fromJson(guestJSONArray, collectionType);
//            Log.v("newGuests",newGuests.toString());
            guests.addAll(newGuests);
            guestArrayAdapter.notifyDataSetChanged();
            PriseEngine.priseFilter = priseFilter;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getSharedEventUsersList(int eventId, int byUserId, Context context) {
        try {
            JsonObject usersJson = Ion.with(context)
                    .load(PriseWebAppFactors.URL_SHARED_EVENT_USERNAME_LIST_VIA_JSON)
                    .setBodyParameter("userid", String.valueOf(byUserId))
                    .setBodyParameter("eventid", String.valueOf(eventId))
                    .asJsonObject()
                    .get();
            ArrayList<String> usersList = new ArrayList<String>();
            for (JsonElement u : usersJson.getAsJsonArray("users")) {
                usersList.add(u.getAsString());
            }
            return usersList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUserToEvent(int eventId, int byUserId, String username, Context context) {
        try {
            return Boolean.parseBoolean(Ion.with(context)
                    .load(PriseWebAppFactors.URL_INSERT_USER_TO_EVENT)
                    .setBodyParameter("userid", String.valueOf(byUserId))
                    .setBodyParameter("eventid", String.valueOf(eventId))
                    .setBodyParameter("username", username)
                    .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                    .get().trim());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUserThisEvent(int eventId, int byUserId, String username, Context context) {
        try {
            return Boolean.parseBoolean(Ion.with(context)
                    .load(PriseWebAppFactors.URL_DELETE_SHRBY_USERNAME)
                    .setBodyParameter("userid", String.valueOf(byUserId))
                    .setBodyParameter("eventid", String.valueOf(eventId))
                    .setBodyParameter("username", username)
                    .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                    .get().trim());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUsernameById(int userId, Context context) {
        try {
            return Ion.with(context)
                    .load(PriseWebAppFactors.URL_GET_EVENT_HOST_USERNAME)
                    .setBodyParameter("userid", String.valueOf(userId))
                    .asString()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public static void saveLog(int eventId, int byUserId, String lByUserName, String guestName, int ConditionNo, int guestNo, Context context) {

        Builders.Any.U ionBuilder = Ion.with(context)
                .load(PriseWebAppFactors.URL_SAVE_LOG)
                .setBodyParameter("userid", String.valueOf(byUserId))
                .setBodyParameter("eventid", String.valueOf(eventId))
                .setBodyParameter("user_actname", lByUserName)
                .setBodyParameter("guestname", guestName);
        try {
            switch (ConditionNo) {
                case 0:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(11))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    editGuestStatus(2, guestNo, byUserId, eventId, context);
                    break;
                case 1:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(13)) //swap 12
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    editGuestStatus(1, guestNo, byUserId, eventId, context);
                    break;
                case 2:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(12)) //swap 13
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    editGuestStatus(2, guestNo, byUserId, eventId, context);
                    break;
                case 3:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(50))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 4:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(51))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 5:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(54))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                default:
                    Log.v("saveLog", "Unexpected ConditionNo : " + ConditionNo);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[0]11 = Going to the seat with..
        //[1]12 = Quiting from the event with..
        //[2]13 = .. Already at seat.
        //[3]50 = .. is Ready to Receive the Award.
        //[4]51 = .. is Unready to Receive the Award.
        //[-]52 = .. is Absent << May not use.
        //[6]53 = .. is Already Received the Award << May not use.
        //[5]54 = .. is Already Quited the event
    }

    public static void saveLog(int eventId, int byUserId, String lByUserName, String guestName, int ConditionNo, Context context) {

        Builders.Any.U ionBuilder = Ion.with(context)
                .load(PriseWebAppFactors.URL_SAVE_LOG)
                .setBodyParameter("userid", String.valueOf(byUserId))
                .setBodyParameter("eventid", String.valueOf(eventId))
                .setBodyParameter("user_actname", lByUserName)
                .setBodyParameter("guestname", guestName);
        try {
            switch (ConditionNo) {
                case 0:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(11))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 1:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(12))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 2:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(13))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 3:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(50))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 4:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(51))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 5:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(54))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                case 99:
                    ionBuilder.setBodyParameter("logtype", String.valueOf(99))
                            .asString(Charset.forName(PriseWebAppFactors.CHARSET_UTF8))
                            .get();
                    break;
                default:
                    Log.v("saveLog", "Unexpected ConditionNo : " + ConditionNo);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[0]11 = Going to the seat with..
        //[1]12 = Quiting from the event with..
        //[2]13 = .. Already at seat.
        //[3]50 = .. is Ready to Receive the Award.
        //[4]51 = .. is Unready to Receive the Award.
        //[-]52 = .. is Absent << May not use.
        //[6]53 = .. is Already Received the Award << May not use.
        //[5]54 = .. is Already Quited the event
    }

    public static void emptyLog(int byUserId, int eventId, Context context) {
        try {
            Ion.with(context)
                    .load(PriseWebAppFactors.URL_EMPTY_LOG)
                    .setBodyParameter("userid", String.valueOf(byUserId))
                    .setBodyParameter("eventid", String.valueOf(eventId))
                    .asString()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<EventLogging> getLog(int byUserId, int eventId, Context context) {
        ArrayList<EventLogging> logList = null;
        try {
            String log = Ion.with(context)
                    .load(PriseWebAppFactors.URL_GET_LOG)
                    .setBodyParameter("userid", String.valueOf(byUserId))
                    .setBodyParameter("eventid", String.valueOf(eventId))
                    .asString()
                    .get();
            logList = new Gson().fromJson(log, new TypeToken<ArrayList<EventLogging>>() {
            }.getType());
            if (logList != null) {
//                Collections.reverse(logList);
            }
            return logList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
