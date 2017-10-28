package com.example.baly.currencyexchange;

/**
 * The interface class responsible for callback.
 */

interface AsyncResponse {
    /**
     * @param message
     *      stores the error message in cases of failures.
     * **/
    void processFinish(String message, String result);
}
