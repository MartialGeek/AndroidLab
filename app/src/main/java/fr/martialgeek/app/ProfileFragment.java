package fr.martialgeek.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import fr.martialgeek.app.transport.MartialGeekHttpClient;


public class ProfileFragment extends Fragment {
    private WeakReference<ProfileTask> mProfileTask;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ProfileTask profileTask = new ProfileTask(this);
        mProfileTask = new WeakReference<ProfileTask>(profileTask);
        mProfileTask.get().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getStringArray(R.array.menu_labels)[0]);

        return inflater.inflate(R.layout.fragment_profile, container, false);
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

    static private class ProfileTask extends MartialGeekHttpClient {
        public ProfileTask(ProfileFragment fragment) {
            super(fragment, "http://lab.martialgeek.fr/contact.php");
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        protected void onPostExecute(String response) {
            ProfileFragment placeholderFragment = (ProfileFragment) getPlaceholderFragment();
            placeholderFragment.renderFields(response);
            mHttpClient.close();
        }
    }
}
