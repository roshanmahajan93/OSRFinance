package com.example.chetu.osrfinance;

public interface Config {

    // When you are using two simulator for testing this application.
    // Make SECOND_SIMULATOR value true when opening/installing application in second simulator
    // Actually we are validating/saving device data on IMEI basis.
    // if it is true IMEI number change for second simulator

    static final boolean SECOND_SIMULATOR = false;

    // CONSTANTS

    // Server Url absolute url where php files are placed.
    static final String YOUR_SERVER_URL = "http://petalsinfotech.com/osrfinance/";

    // Google project id
    static final String GOOGLE_SENDER_ID = "356050296993";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    public static final String FILE_UPLOAD_URL = "http://softradius.com.md-64.webhostbox.net/vinod_app/upload/file_upload.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";


    // Broadcast reciever name to show gcm registration messages on screen 
    static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_REGISTRATION_MESSAGE";

    // Broadcast reciever name to show user messages on screen
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_MESSAGE";

    // Parse server message with this name
    static final String EXTRA_MESSAGE = "message";


    //------------androidhive------------------
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 102;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String EMAIL = "Chetanborse83@gmail.com";
    public static final String PASSWORD = "Sentient12345";
}




