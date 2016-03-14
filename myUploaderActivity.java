package com.weebly.docrosby.listtaker;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class myUploaderActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Project mProject;
    public myDBHandler mDBHandler;
    private static final int CHOOSE_ACCOUNT = 0;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_FINISH = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    public static final String EXTRA_PROJECT_ID = "com.weebly.docrosby.listtaker.project_id";
    private static String accountName = "";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID projectId = (UUID)getIntent().getSerializableExtra(EXTRA_PROJECT_ID);
        mProject = ProjectLab.get().getProject(projectId);
        mDBHandler = new myDBHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(accountName.length()==0){
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},false, null, null, null, null);
            startActivityForResult(intent, CHOOSE_ACCOUNT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(requestCode==REQUEST_CODE_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }
        if(requestCode==REQUEST_CODE_FINISH) {
            if (resultCode == RESULT_OK) {
                return;
            }
        }
        if(requestCode==CHOOSE_ACCOUNT) {
            if(resultCode == RESULT_OK) {
                accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(com.google.android.gms.drive.Drive.API)
                        .addScope(com.google.android.gms.drive.Drive.SCOPE_FILE)
                        .setAccountName(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME))
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
            }
            return;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        }
        catch (SendIntentException e) {}
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        saveFileToDrive();
    }

    //If you put this code inside of the onConnected() method(which would make sense), rather than putting it in it's own method
    //Android will throw an error when creating the IntentSender, saying "Client is not connected"; even though it is connected.
    private void saveFileToDrive() {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult result) {
                if (!result.getStatus().isSuccess()) {
                    return;
                }

                OutputStream outputStream = result.getDriveContents().getOutputStream();
                try {
                    outputStream.write((mDBHandler.generateFileString(mProject)).getBytes());
                }
                catch(IOException io){
                    String s = io.getMessage();
                }
                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder().setMimeType("text/csv").setTitle((mProject.getTitle() + ".csv")).build();
                IntentSender intentSender = null;
                try{
                    intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.getDriveContents())
                            .build(mGoogleApiClient);
                }
                catch(Exception e) {
                    //To catch any client not connected Exception, and return to the MainActivity without crashing the program.
                }
                try {
                    startIntentSenderForResult(intentSender, REQUEST_CODE_FINISH, null, 0, 0, 0);
                }
                catch (SendIntentException e){}
            }
        });
        accountName = "";
        finish();
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
    }
}

