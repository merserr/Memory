package com.example.memory;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //  @StringRes
    //   private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    //   private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        //      mContext = context;
    }

    // Returns the fragment to display for that page
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return Fragment1.newInstance(0, "192.168.1.1 mikrotik");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return Fragment2.newInstance(1, "10.254.154.12 cisco");
            case 2: // Fragment # 1 - This will show SecondFragment
                return Fragment3.newInstance(2, "10.254.154.2 cisco");
            case 3: // Fragment # 1 - This will show SecondFragment
                return Fragment4.newInstance(3, "10.254.154.3 cisco");
            case 4: // Fragment # 0 - This will show FirstFragment different title
                return Fragment2.newInstance(4, "10.254.154.4 cisco");
            case 5: // Fragment # 1 - This will show SecondFragment
                return Fragment1.newInstance(5, "10.254.154.5 cisco");
            case 6: // Fragment # 1 - This will show SecondFragment
                return Fragment2.newInstance(6, "10.254.154.7 cisco");
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SATZ";
            case 1:
                return "VERBE";
            case 2:
                return "NOME";
            case 3:
                return "ADJ";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "7";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}