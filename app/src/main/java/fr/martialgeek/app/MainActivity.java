package fr.martialgeek.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
    private String[] mMenuLabels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuLabels = getResources().getStringArray(R.array.menu_labels);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuLabels));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState == null) {
            try {
                selectItem(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
     * Update the main content by replacing fragments.
     * @throws Exception
     * @param position The menu position
     */
    private void selectItem(int position) throws Exception {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                break;
            case 1:
                fragment = new MembersFragment();
                break;
            default:
                throw new Exception("Invalid position");
        }

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                selectItem(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
