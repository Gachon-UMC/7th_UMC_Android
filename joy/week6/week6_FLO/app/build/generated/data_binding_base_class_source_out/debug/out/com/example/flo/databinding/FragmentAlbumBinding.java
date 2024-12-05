// Generated by view binder compiler. Do not edit!
package com.example.flo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.flo.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAlbumBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout albumActionbarView;

  @NonNull
  public final TextView albumArtistTv;

  @NonNull
  public final TabLayout albumContentTb;

  @NonNull
  public final ViewPager2 albumContentVp;

  @NonNull
  public final CardView albumCoverCv;

  @NonNull
  public final ImageView albumCoverIv;

  @NonNull
  public final TextView albumGenreDetailTv;

  @NonNull
  public final TextView albumGenreTv;

  @NonNull
  public final ConstraintLayout albumInfoView;

  @NonNull
  public final TextView albumReleaseDateTv;

  @NonNull
  public final TextView albumTitleTv;

  @NonNull
  public final View borderBottom;

  @NonNull
  public final TextView borderTv;

  @NonNull
  public final TextView borderTv2;

  @NonNull
  public final ImageView btnArrowBackIv;

  @NonNull
  public final ImageView btnPlayerMoreIv;

  @NonNull
  public final ImageView icMyLikeOffIv;

  @NonNull
  public final ConstraintLayout mainView;

  @NonNull
  public final ImageView widgetBlackPlayIv;

  private FragmentAlbumBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout albumActionbarView, @NonNull TextView albumArtistTv,
      @NonNull TabLayout albumContentTb, @NonNull ViewPager2 albumContentVp,
      @NonNull CardView albumCoverCv, @NonNull ImageView albumCoverIv,
      @NonNull TextView albumGenreDetailTv, @NonNull TextView albumGenreTv,
      @NonNull ConstraintLayout albumInfoView, @NonNull TextView albumReleaseDateTv,
      @NonNull TextView albumTitleTv, @NonNull View borderBottom, @NonNull TextView borderTv,
      @NonNull TextView borderTv2, @NonNull ImageView btnArrowBackIv,
      @NonNull ImageView btnPlayerMoreIv, @NonNull ImageView icMyLikeOffIv,
      @NonNull ConstraintLayout mainView, @NonNull ImageView widgetBlackPlayIv) {
    this.rootView = rootView;
    this.albumActionbarView = albumActionbarView;
    this.albumArtistTv = albumArtistTv;
    this.albumContentTb = albumContentTb;
    this.albumContentVp = albumContentVp;
    this.albumCoverCv = albumCoverCv;
    this.albumCoverIv = albumCoverIv;
    this.albumGenreDetailTv = albumGenreDetailTv;
    this.albumGenreTv = albumGenreTv;
    this.albumInfoView = albumInfoView;
    this.albumReleaseDateTv = albumReleaseDateTv;
    this.albumTitleTv = albumTitleTv;
    this.borderBottom = borderBottom;
    this.borderTv = borderTv;
    this.borderTv2 = borderTv2;
    this.btnArrowBackIv = btnArrowBackIv;
    this.btnPlayerMoreIv = btnPlayerMoreIv;
    this.icMyLikeOffIv = icMyLikeOffIv;
    this.mainView = mainView;
    this.widgetBlackPlayIv = widgetBlackPlayIv;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAlbumBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAlbumBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_album, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAlbumBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.album_actionbar_view;
      ConstraintLayout albumActionbarView = ViewBindings.findChildViewById(rootView, id);
      if (albumActionbarView == null) {
        break missingId;
      }

      id = R.id.album_artist_tv;
      TextView albumArtistTv = ViewBindings.findChildViewById(rootView, id);
      if (albumArtistTv == null) {
        break missingId;
      }

      id = R.id.album_content_tb;
      TabLayout albumContentTb = ViewBindings.findChildViewById(rootView, id);
      if (albumContentTb == null) {
        break missingId;
      }

      id = R.id.album_content_vp;
      ViewPager2 albumContentVp = ViewBindings.findChildViewById(rootView, id);
      if (albumContentVp == null) {
        break missingId;
      }

      id = R.id.album_cover_cv;
      CardView albumCoverCv = ViewBindings.findChildViewById(rootView, id);
      if (albumCoverCv == null) {
        break missingId;
      }

      id = R.id.album_cover_iv;
      ImageView albumCoverIv = ViewBindings.findChildViewById(rootView, id);
      if (albumCoverIv == null) {
        break missingId;
      }

      id = R.id.album_genre_detail_tv;
      TextView albumGenreDetailTv = ViewBindings.findChildViewById(rootView, id);
      if (albumGenreDetailTv == null) {
        break missingId;
      }

      id = R.id.album_genre_tv;
      TextView albumGenreTv = ViewBindings.findChildViewById(rootView, id);
      if (albumGenreTv == null) {
        break missingId;
      }

      id = R.id.album_info_view;
      ConstraintLayout albumInfoView = ViewBindings.findChildViewById(rootView, id);
      if (albumInfoView == null) {
        break missingId;
      }

      id = R.id.album_release_date_tv;
      TextView albumReleaseDateTv = ViewBindings.findChildViewById(rootView, id);
      if (albumReleaseDateTv == null) {
        break missingId;
      }

      id = R.id.album_title_tv;
      TextView albumTitleTv = ViewBindings.findChildViewById(rootView, id);
      if (albumTitleTv == null) {
        break missingId;
      }

      id = R.id.border_bottom;
      View borderBottom = ViewBindings.findChildViewById(rootView, id);
      if (borderBottom == null) {
        break missingId;
      }

      id = R.id.border_tv;
      TextView borderTv = ViewBindings.findChildViewById(rootView, id);
      if (borderTv == null) {
        break missingId;
      }

      id = R.id.border_tv_2;
      TextView borderTv2 = ViewBindings.findChildViewById(rootView, id);
      if (borderTv2 == null) {
        break missingId;
      }

      id = R.id.btn_arrow_back_iv;
      ImageView btnArrowBackIv = ViewBindings.findChildViewById(rootView, id);
      if (btnArrowBackIv == null) {
        break missingId;
      }

      id = R.id.btn_player_more_iv;
      ImageView btnPlayerMoreIv = ViewBindings.findChildViewById(rootView, id);
      if (btnPlayerMoreIv == null) {
        break missingId;
      }

      id = R.id.ic_my_like_off_iv;
      ImageView icMyLikeOffIv = ViewBindings.findChildViewById(rootView, id);
      if (icMyLikeOffIv == null) {
        break missingId;
      }

      id = R.id.main_view;
      ConstraintLayout mainView = ViewBindings.findChildViewById(rootView, id);
      if (mainView == null) {
        break missingId;
      }

      id = R.id.widget_black_play_iv;
      ImageView widgetBlackPlayIv = ViewBindings.findChildViewById(rootView, id);
      if (widgetBlackPlayIv == null) {
        break missingId;
      }

      return new FragmentAlbumBinding((ConstraintLayout) rootView, albumActionbarView,
          albumArtistTv, albumContentTb, albumContentVp, albumCoverCv, albumCoverIv,
          albumGenreDetailTv, albumGenreTv, albumInfoView, albumReleaseDateTv, albumTitleTv,
          borderBottom, borderTv, borderTv2, btnArrowBackIv, btnPlayerMoreIv, icMyLikeOffIv,
          mainView, widgetBlackPlayIv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}