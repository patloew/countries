package com.patloew.countries.util.bindingadapter;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.v4.view.ViewPager;

/* Copyright 2017 Tailored Media GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class ViewPagerBindingAdapter {

    @InverseBindingAdapter(attribute = "selectedPagePosition", event = "selectedPagePositionAttrChanged")
    public static int captureSelectedPagePosition(ViewPager viewPager) {
        return viewPager.getCurrentItem();
    }

    @BindingAdapter("selectedPagePosition")
    public static void setSelectedPagePosition(ViewPager viewPager, int position) {
        viewPager.setCurrentItem(position);
    }


    @BindingAdapter(value = {"onPageChangeListener", "selectedPagePositionAttrChanged"}, requireAll = false)
    public static void setOnPageChangeListener(ViewPager viewPager, final ViewPager.OnPageChangeListener pageChangeListener, final InverseBindingListener attrChanged) {
        if (pageChangeListener == null && attrChanged == null) {
            viewPager.setOnPageChangeListener(null);
        } else {
            viewPager.setOnPageChangeListener(new BindingOnPageChangedListener(pageChangeListener, attrChanged));
        }
    }

    private static class BindingOnPageChangedListener implements ViewPager.OnPageChangeListener {

        private final ViewPager.OnPageChangeListener pageChangeListener;
        private final InverseBindingListener inverseBindingListener;

        BindingOnPageChangedListener(ViewPager.OnPageChangeListener pageChangeListener, InverseBindingListener inverseBindingListener) {
            this.pageChangeListener = pageChangeListener;
            this.inverseBindingListener = inverseBindingListener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(pageChangeListener != null) { pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels); }
        }

        @Override
        public void onPageSelected(int position) {
            if(pageChangeListener != null) { pageChangeListener.onPageSelected(position); }
            if(inverseBindingListener != null) { inverseBindingListener.onChange(); }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(pageChangeListener != null) { pageChangeListener.onPageScrollStateChanged(state); }
        }
    }
}
