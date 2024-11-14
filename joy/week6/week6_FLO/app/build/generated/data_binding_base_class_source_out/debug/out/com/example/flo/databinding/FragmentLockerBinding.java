// Generated by view binder compiler. Do not edit!
package com.example.flo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.flo.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentLockerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout lockerActionbarView;

  @NonNull
  public final TabLayout lockerContentTb;

  @NonNull
  public final ViewPager2 lockerContentVp;

  @NonNull
  public final TextView lockerLoginTv;

  @NonNull
  public final TextView lockerMainTv;

  @NonNull
  public final ImageView lockerScreenShareIcon;

  @NonNull
  public final ImageView lockerSettingIv;

  private FragmentLockerBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout lockerActionbarView, @NonNull TabLayout lockerContentTb,
      @NonNull ViewPager2 lockerContentVp, @NonNull TextView lockerLoginTv,
      @NonNull TextView lockerMainTv, @NonNull ImageView lockerScreenShareIcon,
      @NonNull ImageView lockerSettingIv) {
    this.rootView = rootView;
    this.lockerActionbarView = lockerActionbarView;
    this.lockerContentTb = lockerContentTb;
    this.lockerContentVp = lockerContentVp;
    this.lockerLoginTv = lockerLoginTv;
    this.lockerMainTv = lockerMainTv;
    this.lockerScreenShareIcon = lockerScreenShareIcon;
    this.lockerSettingIv = lockerSettingIv;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentLockerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentLockerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_locker, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentLockerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.locker_actionbar_view;
      ConstraintLayout lockerActionbarView = ViewBindings.findChildViewById(rootView, id);
      if (lockerActionbarView == null) {
        break missingId;
      }

      id = R.id.locker_content_tb;
      TabLayout lockerContentTb = ViewBindings.findChildViewById(rootView, id);
      if (lockerContentTb == null) {
        break missingId;
      }

      id = R.id.locker_content_vp;
      ViewPager2 lockerContentVp = ViewBindings.findChildViewById(rootView, id);
      if (lockerContentVp == null) {
        break missingId;
      }

      id = R.id.locker_login_tv;
      TextView lockerLoginTv = ViewBindings.findChildViewById(rootView, id);
      if (lockerLoginTv == null) {
        break missingId;
      }

      id = R.id.locker_main_tv;
      TextView lockerMainTv = ViewBindings.findChildViewById(rootView, id);
      if (lockerMainTv == null) {
        break missingId;
      }

      id = R.id.locker_screen_share_icon;
      ImageView lockerScreenShareIcon = ViewBindings.findChildViewById(rootView, id);
      if (lockerScreenShareIcon == null) {
        break missingId;
      }

      id = R.id.locker_setting_iv;
      ImageView lockerSettingIv = ViewBindings.findChildViewById(rootView, id);
      if (lockerSettingIv == null) {
        break missingId;
      }

      return new FragmentLockerBinding((ConstraintLayout) rootView, lockerActionbarView,
          lockerContentTb, lockerContentVp, lockerLoginTv, lockerMainTv, lockerScreenShareIcon,
          lockerSettingIv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
