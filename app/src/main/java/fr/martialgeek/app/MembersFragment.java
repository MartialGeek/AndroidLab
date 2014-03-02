package fr.martialgeek.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

public class MembersFragment extends Fragment {

    WeakReference<MembersTask> mMemberTask;

    public MembersFragment() {
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

    static private class MembersTask extends MartialGeekHttpClient {

        public MembersTask(MembersFragment fragment) {
            super(fragment, "http://lab.martialgeek.fr/members.json");
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        protected void onPostExecute(String response) {
            MembersFragment fragment = (MembersFragment) getPlaceholderFragment();
            fragment.renderFields(response);
            mHttpClient.close();
        }
    }
}
