package bd.org.bitm.mad.batch33.tourmate.model;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mobile App Develop on 5/30/2018.
 */

public class Offline extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
