package fr.martialgeek.app.transport;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Abstract class to request the MartialGeek webservices.
 * @author Martial Saunois
 */
public abstract class MartialGeekHttpClient extends AsyncTask<Void, Void, String> {
    protected String mUrl;
    protected AndroidHttpClient mHttpClient;
    protected WeakReference<Fragment> mWeakReferenceFragment;

    /**
     * Constructor.
     * @param fragment The fragment instance
     * @param url The target URL
     */
    public MartialGeekHttpClient(Fragment fragment, String url) {
        mWeakReferenceFragment = new WeakReference<Fragment>(fragment);
        mUrl = url;
    }

    /**
     * Prepare the HTTP client.
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected void onPreExecute() {
        mHttpClient = AndroidHttpClient.newInstance("Mozilla 5.0");
    }

    /**
     * Execute the request and returns the response.
     * @param params The parameters send by the method execute().
     * @return The string representation of the response.
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpResponse httpResponse = mHttpClient.execute(new HttpGet(mUrl));

            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This task contains the logic to update the view with the result.
     * @param response The HTTP response
     */
    @Override
    protected abstract void onPostExecute(String response);

    /**
     * Get the fragment instance.
     * @return The fragment instance
     */
    protected Fragment getPlaceholderFragment() {
        return mWeakReferenceFragment.get();
    }
}
