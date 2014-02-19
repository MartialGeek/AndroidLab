package fr.martialgeek.app;

import android.annotation.TargetApi;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        private WeakReference<ContactTask> mContactTask;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            ContactTask contactTask = new ContactTask(this);
            mContactTask = new WeakReference<ContactTask>(contactTask);
            getContact();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        public void renderFields(String response) {
            EditText emailField = (EditText) getView().findViewById(R.id.email);
            EditText firstnameField = (EditText) getView().findViewById(R.id.firstname);
            EditText lastnameField = (EditText) getView().findViewById(R.id.lastname);

            try {
                JSONObject toJSON = new JSONObject(response);
                emailField.setText(toJSON.getString("email"));
                firstnameField.setText(toJSON.getString("firstname"));
                lastnameField.setText(toJSON.getString("lastname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private ContactTask getContactTask() throws MainActivityException {
            if (mContactTask.get() == null) {
                throw new MainActivityException();
            }

            return mContactTask.get();
        }

        private void getContact() {
            ContactTask contactTask;

            try {
                contactTask = getContactTask();
                contactTask.execute();
            } catch (MainActivityException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ContactTask extends AsyncTask<Void, Void, String> {
        private WeakReference<PlaceholderFragment> mPlaceholderFragment;
        private AndroidHttpClient mHttpClient;

        private ContactTask(PlaceholderFragment fragment) {
            mPlaceholderFragment = new WeakReference<PlaceholderFragment>(fragment);
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
                HttpResponse httpResponse = mHttpClient.execute(new HttpGet("http://lab.martialgeek.fr/contact.php"));

                return EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        protected void onPostExecute(String response) {
            PlaceholderFragment placeholderFragment = getPlaceholderFragment();
            placeholderFragment.renderFields(response);
            mHttpClient.close();
        }

        private PlaceholderFragment getPlaceholderFragment() {
            if (mPlaceholderFragment.get() != null) {
                return mPlaceholderFragment.get();
            }

            return null;
        }
    }

    public class MainActivityException extends Exception {

    }
}
