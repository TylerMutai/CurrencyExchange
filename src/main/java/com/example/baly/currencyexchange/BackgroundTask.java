package com.example.baly.currencyexchange;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Class used for primarily initiating Network requests in another thread(AsyncTask).
 */

class BackgroundTask extends AsyncTask<Void, Void, String> {
    String opMsg = "An error occured. Please check your internet connection or try again later:(";
    AsyncResponse delegate = null;
    NetworkConnection networkConnection;

    /**
     * The constructor call
     * @param delegate
     *      refers to the interface AsyncResponse, whose processFinish method is called by the onPostExecute.
     *
     * @param urlPattern
     *      refers to the url pattern as per the public api.
     * **/
    BackgroundTask(AsyncResponse delegate, String urlPattern){
        this.delegate = delegate;
        String urlPattern1 = urlPattern;

        //initiate a network connection request with the new url pattern.
        networkConnection = new NetworkConnection(urlPattern1);

    }

    @Override
    protected String doInBackground(Void...params) {
        networkConnection.connectionMethod();
        String result = readStringData();
        networkConnection.httpURLConnection.disconnect();
        return result;
    }
    @Override
    protected void onPostExecute(String result){
        delegate.processFinish(opMsg, result);
    }

    private String readStringData(){
       StringBuilder stringBuilder = new StringBuilder();
       try {
           InputStream is = networkConnection.httpURLConnection.getInputStream();
           BufferedReader br = new BufferedReader(new InputStreamReader(is));
           String fetch;

           while ((fetch = br.readLine()) != null) {
               stringBuilder.append(fetch);
           }
           is.close();
           br.close();
       }
       catch (Exception e){
           opMsg = "An error occured reading from server. Check your internet connection or try again later :(";
       }
       return stringBuilder.toString();
   }
}
