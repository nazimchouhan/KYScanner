package com.example.kyscanner.server;



import android.content.Context;
import android.util.Log;

import com.example.kyscanner.model.UserModel;
import com.example.kyscanner.security.FetchUserDetail;
import com.example.kyscanner.security.UpdateKyIdworker;
import com.example.kyscanner.security.UserDatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KYIdManager {
    public static final int BatchSize =2;
    private static KYIdManager instance;
    private final kyIdRequestServer requestServer;
    private final List<String> kyidList = new ArrayList<>();
    private final Context context;
    private final List<String> kyidlist;
    private final Set<String> ScannedKyIds;
    private int totalScan = 0;
    private KYIdManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null!"); // Prevent issues
        }
        this.context = context.getApplicationContext();
        this.requestServer = kyIdRequestServer.getInstance(this.context);
        this.kyidlist = new ArrayList<>();
        this.ScannedKyIds = new HashSet<>();
    }

    public static synchronized KYIdManager getInstance(Context context) {
        if (instance == null) {
            instance = new KYIdManager(context);
        }
        return instance;
    }
    public void addScannedKyIDs(String KyID) {
        if (!ScannedKyIds.contains(KyID)) {
            Log.d("KYIdManager", "Adding KYID: " + KyID);

            // Add KYID to lists
            ScannedKyIds.add(KyID);
            kyidlist.add(KyID);
            totalScan++;

            Log.e("KYIdManager", "Current kyidlist: " + kyidlist);

            // Check if batch size is reached
            if (totalScan == BatchSize) {
                // Create a copy before clearing
                List<String> batchCopy = new ArrayList<>(kyidlist);
                kyIdRequestServer kyIdRequestServer=new kyIdRequestServer(context);
                kyIdRequestServer.sendKYIDBatch(batchCopy);
                // Reset tracking variables
                kyidlist.clear();
                totalScan = 0;

                Log.e("KYIdManager", "Sending batchCopy kyidlist: " + batchCopy);

                // Send batch to server

            }
        } else {
            Log.d("KYIdManager", "Duplicate KyID ignored: " + KyID);
        }
    }
}
