package com.siyehua.extendtoolsbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> list;
	public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}

	/**
	 * 添加一个碎片
	 * 
	 * @param item
	 *            一个碎片
	 */
	public void addItem(Fragment item) {
		if (list == null) {
			list = new ArrayList<Fragment>();
		}
		list.add(item);
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

}
