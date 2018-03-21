package com.centaury.jalurangkot.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.centaury.jalurangkot.ArmadaTerdekatFragment;
import com.centaury.jalurangkot.JalurMikroletFragment;
import com.centaury.jalurangkot.R;
import com.centaury.jalurangkot.TempatPentingFragment;

/**
 * Created by Centaury on 20/02/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public enum TabItem {
        ARMADA_TERDEKAT(ArmadaTerdekatFragment.class, R.string.title_armada_terdekat),
        JALUR_MIKROLET(JalurMikroletFragment.class, R.string.title_jalur_mikrolet),
        TEMPAT_PENTING(TempatPentingFragment.class, R.string.title_tempat_penting);

        private final Class<? extends Fragment> fragmentClass;
        private final int titleResId;

        TabItem(Class<? extends Fragment> fragmentClass, @StringRes int titleResId) {
            this.fragmentClass = fragmentClass;
            this.titleResId = titleResId;
        }
    }

    private final TabItem[] tabItems;
    private final Context context;

    public PagerAdapter(FragmentManager fragmentManager, Context context, TabItem... tabItems) {
        super(fragmentManager);
        this.context = context;
        this.tabItems = tabItems;
    }

    @Override
    public Fragment getItem(int position) {
        return newInstance(tabItems[position].fragmentClass);
    }

    private Fragment newInstance(Class<? extends Fragment> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("fragment must have public no-arg constructor: " + fragmentClass.getName(), e);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabItems[position].titleResId);
    }

    @Override
    public int getCount() {
        return tabItems.length;
    }
}
