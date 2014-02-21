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

public abstract class MartialGeekHttpClient extends AsyncTask<Void, Void, String> {
    protected String mUrl;
    protected AndroidHttpClient mHttpClient;
    protected WeakReference<Fragment> mWeakReferenceFragment;

    public MartialGeekHttpClient(Fragment fragment, String url) {
        mWeakReferenceFragment = new WeakReference<Fragment>(fragment);
        mUrl = url;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected void onPreExecute() {
        mHttpClient = AndroidHttpClient.newInstance("Mozilla 5.0");
    }

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

    @Override
    protected abstract void onPostExecute(String response);

    protected Fragment getPlaceholderFragment() {
        return mWeakReferenceFragment.get();
    }
}
