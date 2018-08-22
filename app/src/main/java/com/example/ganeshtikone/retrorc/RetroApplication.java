package com.example.ganeshtikone.retrorc;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Ganesh Tikone on 11/7/18.
 * Company: Silicus Technologies Pvt. Ltd.
 * Email: ganesh.tikone@silicus.com
 * Class:
 */
public class RetroApplication extends Application {


  @Override
  public void onCreate() {
    super.onCreate();

    initRealmDatabase();
  }

  /**
   * Initialise Realm Database
   */
  private void initRealmDatabase() {

    Realm.init(this);

    RealmConfiguration config = new RealmConfiguration.Builder()
        .name(getString(R.string.app_name))
        .schemaVersion(0)
        .build();

    // Use the config
    Realm.getInstance(config);
  }
}
