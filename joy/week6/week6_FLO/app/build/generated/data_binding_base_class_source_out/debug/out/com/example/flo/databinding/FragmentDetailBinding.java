// Generated by view binder compiler. Do not edit!
package com.example.flo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.flo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDetailBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextView albumAgencyDetailTv;

  @NonNull
  public final TextView albumAgencyTv;

  @NonNull
  public final TextView albumArtistDetailTv;

  @NonNull
  public final TextView albumArtistTv;

  @NonNull
  public final TextView albumDistributorDetailTv;

  @NonNull
  public final TextView albumDistributorTv;

  @NonNull
  public final ConstraintLayout albumInfoView;

  @NonNull
  public final TextView albumIntroduceDetailTv;

  @NonNull
  public final ConstraintLayout albumIntroduceDetailView;

  @NonNull
  public final TextView albumIntroduceSubtitleTv;

  @NonNull
  public final TextView albumIntroduceTitleTv;

  @NonNull
  public final TextView albumTitleDetailTv;

  @NonNull
  public final TextView albumTitleTv;

  @NonNull
  public final ConstraintLayout horizontalView;

  private FragmentDetailBinding(@NonNull ScrollView rootView, @NonNull TextView albumAgencyDetailTv,
      @NonNull TextView albumAgencyTv, @NonNull TextView albumArtistDetailTv,
      @NonNull TextView albumArtistTv, @NonNull TextView albumDistributorDetailTv,
      @NonNull TextView albumDistributorTv, @NonNull ConstraintLayout albumInfoView,
      @NonNull TextView albumIntroduceDetailTv, @NonNull ConstraintLayout albumIntroduceDetailView,
      @NonNull TextView albumIntroduceSubtitleTv, @NonNull TextView albumIntroduceTitleTv,
      @NonNull TextView albumTitleDetailTv, @NonNull TextView albumTitleTv,
      @NonNull ConstraintLayout horizontalView) {
    this.rootView = rootView;
    this.albumAgencyDetailTv = albumAgencyDetailTv;
    this.albumAgencyTv = albumAgencyTv;
    this.albumArtistDetailTv = albumArtistDetailTv;
    this.albumArtistTv = albumArtistTv;
    this.albumDistributorDetailTv = albumDistributorDetailTv;
    this.albumDistributorTv = albumDistributorTv;
    this.albumInfoView = albumInfoView;
    this.albumIntroduceDetailTv = albumIntroduceDetailTv;
    this.albumIntroduceDetailView = albumIntroduceDetailView;
    this.albumIntroduceSubtitleTv = albumIntroduceSubtitleTv;
    this.albumIntroduceTitleTv = albumIntroduceTitleTv;
    this.albumTitleDetailTv = albumTitleDetailTv;
    this.albumTitleTv = albumTitleTv;
    this.horizontalView = horizontalView;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.album_agency_detail_tv;
      TextView albumAgencyDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumAgencyDetailTv == null) {
        break missingId;
      }

      id = R.id.album_agency_tv;
      TextView albumAgencyTv = ViewBindings.findChildViewById(rootView, id);
      if (albumAgencyTv == null) {
        break missingId;
      }

      id = R.id.album_artist_detail_tv;
      TextView albumArtistDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumArtistDetailTv == null) {
        break missingId;
      }

      id = R.id.album_artist_tv;
      TextView albumArtistTv = ViewBindings.findChildViewById(rootView, id);
      if (albumArtistTv == null) {
        break missingId;
      }

      id = R.id.album_distributor_detail_tv;
      TextView albumDistributorDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumDistributorDetailTv == null) {
        break missingId;
      }

      id = R.id.album_distributor_tv;
      TextView albumDistributorTv = ViewBindings.findChildViewById(rootView, id);
      if (albumDistributorTv == null) {
        break missingId;
      }

      id = R.id.album_info_view;
      ConstraintLayout albumInfoView = ViewBindings.findChildViewById(rootView, id);
      if (albumInfoView == null) {
        break missingId;
      }

      id = R.id.album_introduce_detail_tv;
      TextView albumIntroduceDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumIntroduceDetailTv == null) {
        break missingId;
      }

      id = R.id.album_introduce_detail_view;
      ConstraintLayout albumIntroduceDetailView = ViewBindings.findChildViewById(rootView, id);
      if (albumIntroduceDetailView == null) {
        break missingId;
      }

      id = R.id.album_introduce_subtitle_tv;
      TextView albumIntroduceSubtitleTv = ViewBindings.findChildViewById(rootView, id);
      if (albumIntroduceSubtitleTv == null) {
        break missingId;
      }

      id = R.id.album_introduce_title_tv;
      TextView albumIntroduceTitleTv = ViewBindings.findChildViewById(rootView, id);
      if (albumIntroduceTitleTv == null) {
        break missingId;
      }

      id = R.id.album_title_detail_tv;
      TextView albumTitleDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumTitleDetailTv == null) {
        break missingId;
      }

      id = R.id.album_title_tv;
      TextView albumTitleTv = ViewBindings.findChildViewById(rootView, id);
      if (albumTitleTv == null) {
        break missingId;
      }

      id = R.id.horizontal_view;
      ConstraintLayout horizontalView = ViewBindings.findChildViewById(rootView, id);
      if (horizontalView == null) {
        break missingId;
      }

      return new FragmentDetailBinding((ScrollView) rootView, albumAgencyDetailTv, albumAgencyTv,
          albumArtistDetailTv, albumArtistTv, albumDistributorDetailTv, albumDistributorTv,
          albumInfoView, albumIntroduceDetailTv, albumIntroduceDetailView, albumIntroduceSubtitleTv,
          albumIntroduceTitleTv, albumTitleDetailTv, albumTitleTv, horizontalView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
