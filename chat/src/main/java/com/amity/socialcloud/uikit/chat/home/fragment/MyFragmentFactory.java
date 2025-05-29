package com.amity.socialcloud.uikit.chat.home.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

public class MyFragmentFactory extends FragmentFactory {
        @NonNull
        @Override
        public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                if (className.equals(AmityChatHomePageFragment.class.getName())) {
                        return new AmityChatHomePageFragment();
                }
                return super.instantiate(classLoader, className);
        }
}