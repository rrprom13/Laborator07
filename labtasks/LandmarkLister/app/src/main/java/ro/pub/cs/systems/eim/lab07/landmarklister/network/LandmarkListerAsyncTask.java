package ro.pub.cs.systems.eim.lab07.landmarklister.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.lab07.landmarklister.controller.LandmarkInformationAdapter;
import ro.pub.cs.systems.eim.lab07.landmarklister.general.Constants;
import ro.pub.cs.systems.eim.lab07.landmarklister.model.LandmarkInformation;

public class LandmarkListerAsyncTask extends AsyncTask<String, Void, List<LandmarkInformation>> {

    private ListView landmarkListView;

    public LandmarkListerAsyncTask(ListView landmarkListView) {
        this.landmarkListView = landmarkListView;
    }

    @Override
    protected List<LandmarkInformation> doInBackground(String... params) {

        // TODO exercise 7
        // - create an instance of a HttpClient object
        // - create the URL to the web service, appending the bounding box coordinates and the username to the base Internet address
        // - create an instance of a HttGet object
        // - create an instance of a ReponseHandler object
        // - execute the request, thus obtaining the response
        // - get the JSON object representing the response
        // - get the JSON array (the value corresponding to the "geonames" attribute)
        // - iterate over the results list and create a LandmarkInformation for each element
        HttpClient httpClient = new DefaultHttpClient();
        String urlString = Constants.LANDMARK_LISTER_WEB_SERVICE_INTERNET_ADDRESS + Constants.NORTH + params[0] + "&" + Constants.SOUTH + params[1] + "&" + Constants.EAST + params[2] + "&" + Constants.WEST + params[3] + "&" + Constants.CREDENTIALS;
        HttpGet httpGet = new HttpGet(urlString);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            String content = httpClient.execute(httpGet, responseHandler);
            final ArrayList<LandmarkInformation> landMarkInformationList = new ArrayList<LandmarkInformation>();
            JSONObject result = new JSONObject(content);
            JSONArray jsonArray = result.getJSONArray(Constants.GEONAMES);

            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject = jsonArray.getJSONObject(k);
                landMarkInformationList.add(new LandmarkInformation(
                        jsonObject.getDouble(Constants.LATITUDE),
                        jsonObject.getDouble(Constants.LONGITUDE),
                        jsonObject.getString(Constants.TOPONYM_NAME),
                        jsonObject.getLong(Constants.POPULATION),
                        jsonObject.getString(Constants.CODE_NAME),
                        jsonObject.getString(Constants.NAME),
                        jsonObject.getString(Constants.WIKIPEDIA_WEB_PAGE_ADDRESS),
                        jsonObject.getString(Constants.COUNTRY_CODE)));
            }

            return landMarkInformationList;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

        @Override
    protected void onPostExecute(List<LandmarkInformation> landmarkInformationList) {

        // TODO exercise 7
        // create a LandmarkInformationAdapter with the array and attach it to the landmarksListView
        landmarkListView.setAdapter(new LandmarkInformationAdapter(
                landmarkListView.getContext(),
                landmarkInformationList));
    }

}
