package net.john.mapsandbox;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by John on 7/25/2016.
 */
public class DbController {
    private static DbController instance;
    private static String host, port, user, pass;
    private static Connection mConn;
    private static Context mContext;

    protected DbController() {
        //override instantiation
    }

    public static DbController getInstance(Context context) {
        if (instance != null) {
            return instance;
        }
        mContext = context;
        instance = new DbController();
        mConn = getConnection();
        return instance;
    }

    private static Connection getConnection() {
        if (mConn != null) {
            return mConn;
        }
        //instantiate db fields from R
        host = mContext.getString(R.string.db_host);
        port = mContext.getString(R.string.db_port);
        user = mContext.getString(R.string.db_user);
        pass = mContext.getString(R.string.db_pass);
        String url = "jdbc:postgresql://public?user=" + user + "&password=" + pass + "&ssl=false";
        try {
            mConn = DriverManager.getConnection(url);
            Log.d("db", "connected to db successfully");
            return mConn;
        } catch (SQLException ex) {
            Log.d("db", ex.toString());
        }
        return null;
    }

}
