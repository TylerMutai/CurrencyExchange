package com.example.baly.currencyexchange;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Class used primarily for setting up.a network connection.
 */

class NetworkConnection {
    /**
     *@param urlPattern
     *  the main url pattern for the site. i.e cryptocompare.com. Additional alterations to the pattern are made via a call to this
     *  class constructor
     * **/
    String urlPattern = "https://";
    HttpURLConnection httpURLConnection;
    public String errorMsg;

    NetworkConnection(String urlPattern) {
        this.urlPattern = this.urlPattern + urlPattern;
        errorMsg = "An error occurred";
    }

    void connectionMethod() {
        try {
            //Set up the URL Object
            URL url = new URL(urlPattern);
            //open the connection. an exception is thrown in case the connection couldn't be established.
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //specify the request method(POST or GET). The cryptocompare api uses the GET request.
            httpURLConnection.setRequestMethod("GET");
            //set a timeout for connection set up. An exception is thrown if the timeout is exceeded.
            httpURLConnection.setConnectTimeout(10000);
            //if(httpURLConnection.getResponseCode()>=100 && httpURLConnection.getResponseCode()==200) {
            //}
            //Exceptions are handled at this point.
        } catch (ConnectException e) {
            errorMsg = "Please check you internet Connection :(";
        } catch (SocketTimeoutException e) {
            errorMsg = "Timeout while reading data. Possible cause is the server might be non-responsive.";
        } catch (SocketException e) {
            errorMsg = "Please check you internet Connection :(";
        } catch (Exception ignored) {

        }
    }

}
