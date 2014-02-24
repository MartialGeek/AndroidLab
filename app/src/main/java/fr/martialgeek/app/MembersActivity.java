package fr.martialgeek.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import fr.martialgeek.app.transport.MartialGeekHttpClient;

public class MembersActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.members_container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_main) {
            Intent mainIntent = new Intent(MembersActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        WeakReference<MembersTask> mMemberTask;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            MembersTask membersTask = new MembersTask(this);
            mMemberTask = new WeakReference<MembersTask>(membersTask);
            mMemberTask.get().execute();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_members, container, false);
        }

        public void renderFields(String response) {
            ListView membersListView = (ListView) getView().findViewById(R.id.members_list);
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> element;

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray members = jsonResponse.getJSONArray("results");

                for (int i = 0; i < members.length(); i++) {
                    JSONObject member = members.getJSONObject(i);
                    element = new HashMap<String, String>();
                    element.put("firstname", member.getString("firstname"));
                    element.put("lastname", member.getString("lastname"));
                    list.add(element);
                }

                ListAdapter adapter = new SimpleAdapter(
                        getActivity(),
                        list,
                        android.R.layout.simple_list_item_2,
                        new String[] {"firstname", "lastname"},
                        new int[] {android.R.id.text1, android.R.id.text2}
                );

                membersListView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static private class MembersTask extends MartialGeekHttpClient {

        public MembersTask(PlaceholderFragment fragment) {
            super(fragment, "http://lab.martialgeek.fr/members.json");
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        protected void onPostExecute(String response) {
            PlaceholderFragment fragment = (PlaceholderFragment) getPlaceholderFragment();
            fragment.renderFields(response);
            mHttpClient.close();
        }
    }
}
