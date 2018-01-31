package com.trag.quartierlatin.prise.extra;

/**
 * Created by QuartierLatin on 8/6/2559.
 */
public class PriseWebAppFactors {
    protected static final String HTTP_HOST = "http://my.nata8ify.me:8080/Prise";
//    protected static final String HTTP_HOST = "http://localhost:8080//Prise";
    public static final String URL_APP_LOGIN = HTTP_HOST+"/Login?opt=app";
    public static final String URL_APP_REGISTER = HTTP_HOST+"/Seem?to=signUp";
    public static final String URL_GUEST_LIST_VIA_JSON = HTTP_HOST+"/AndroidView?opt=allguests";

    public static final String URL_EDIT_GUEST_VIA_JSON = HTTP_HOST+"/GuestEditor?opt=all";
    public static final String URL_DELETE_GUEST = HTTP_HOST+"/GuestEditor?opt=remove";
    public static final String URL_DELETE_EVENT = HTTP_HOST+"/DeleteEvent";
    public static final String URL_EDIT_GUEST_STATUS_VIA_JSON = HTTP_HOST+"/GuestEditor?opt=status";
    public static final String URL_INSERT_GUEST = HTTP_HOST+"/InsertNewGuest";
    public static final String URL_INSERT_USER_TO_EVENT = HTTP_HOST+"/AndroidView?opt=insert_users_to_this_event";
    public static final String URL_INSERT_EVENT = HTTP_HOST+"/CreateNewEvent";
    public static final String URL_EVENT_LIST_VIA_JSON = HTTP_HOST+"/AndroidView?opt=allevents";
    public static final String URL_SHARED_EVENT_LIST_VIA_JSON = HTTP_HOST+"/AndroidView?opt=allevents_with_shared";
    public static final String URL_SHARED_EVENT_USERNAME_LIST_VIA_JSON = HTTP_HOST+"/AndroidView?opt=all_users_in_this_event";
    public static final String URL_GET_EVENT_HOST_USERNAME = HTTP_HOST+"/AndroidView?opt=get_shr_event_hostusername";
    public static final String URL_DELETE_SHRBY_USERNAME = HTTP_HOST+"/AndroidView?opt=del_this_user_inthis_event";
    public static final String URL_SAVE_LOG = HTTP_HOST+"/AndroidView?opt=savelog_action";
    public static final String URL_GET_LOG = HTTP_HOST+"/AndroidView?opt=getlog_action";
    public static final String URL_EMPTY_LOG = HTTP_HOST+"/AndroidView?opt=emptylog";

//    http://52.221.255.26:8080/Prise/AndroidView?opt=allguests&filterno=1&filterargs=1&sortby=1&sorttype=asc&userid=2&eventid=1
    public static final String PARAM_USER_ID = "userid";
    public static final String PARAM_EVENT_ID = "eventid";
    public static final String PARAM_GUEST_NAME = "name";
    public static final String PARAM_GUEST_CORP = "corp";
    public static final String PARAM_GUEST_POSITION = "position";
    public static final String PARAM_GUEST_AWARD = "award";
    public static final String PARAM_GUEST_AWARWD_NUMBER = "awardno";
    public static final String PARAM_GUEST_SEAT_ROW = "seatrow";
    public static final String PARAM_GUEST_SEAT_NO = "seatno";
    public static final String PARAM_GUEST_STATUS = "gstatus";
    public static final String PARAM_GUEST_STATUS_INSERT = "status";
    public static final String PARAM_GUEST_NUMBER = "guestno";
    public static final String PARAM_GUEST_FILTER_NUMBER = "filterno";
    public static final String PARAM_GUEST_FILTER_ARGUMENT = "filterargs";;
    public static final String PARAM_GUEST_SORT_BY_ID = "sortby";
    public static final String PARAM_GUEST_SORT_TYPE_ID = "sorttype";

    public static final String PARAM_EVENT_TITLE = "title";
    public static final String PARAM_EVENT_DESC = "desc";

    public static final String CHARSET_UTF8 = "UTF-8";


}
