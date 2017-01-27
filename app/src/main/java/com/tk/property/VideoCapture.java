package com.tk.property;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoCapture extends Activity {

	private VideoView vv_bsecure = null;

	private ProgressDialog progressDialog;

	private MediaController mediaControls;

	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.lesson_video);

		Intent intent = this.getIntent();
		if (intent != null) {
			String filePath = intent.getStringExtra("filepath");

//			String mimeType = intent.getStringExtra("mimeType");

//			if (mimeType.contains("video")) {

				vv_bsecure = (VideoView) findViewById(R.id.video_view);

				if (mediaControls == null) {
					mediaControls = new MediaController(VideoCapture.this);
				}

				progressDialog = new ProgressDialog(VideoCapture.this);
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(false);
				progressDialog.show();

				try {
					vv_bsecure.setMediaController(mediaControls);
					vv_bsecure.setVideoURI(Uri.parse(filePath));

				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}

				vv_bsecure.requestFocus();
				vv_bsecure.setOnPreparedListener(new OnPreparedListener() {

					public void onPrepared(MediaPlayer mediaPlayer) {
						progressDialog.dismiss();
						vv_bsecure.seekTo(position);
						if (position == 0) {
							vv_bsecure.start();
						} else {
							vv_bsecure.pause();
						}
					}
				});

			}

	//	}
	}

	@Override
	protected void onDestroy() {

		if (vv_bsecure != null) {
			if (vv_bsecure.isPlaying()) {
				vv_bsecure.stopPlayback();
			}
		}

		super.onDestroy();
	}
}
