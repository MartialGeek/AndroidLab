package fr.martialgeek.app;

import android.annotation.TargetApi;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import fr.martialgeek.app.transport.MartialGeekHttpClient;

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
            mContactTask.get().execute();
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
    }

    private static class ContactTask extends MartialGeekHttpClient {
        public ContactTask(PlaceholderFragment fragment) {
            super(fragment, "http://lab.martialgeek.fr/contact.php");
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        protected void onPostExecute(String response) {
            PlaceholderFragment placeholderFragment = (PlaceholderFragment) getPlaceholderFragment();
            placeholderFragment.renderFields(response);
            mHttpClient.close();
        }
    }
}
