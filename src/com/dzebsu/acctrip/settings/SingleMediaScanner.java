package com.dzebsu.acctrip.settings;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

/*
 * scan just written file to make it visible for PC
 */
public class SingleMediaScanner implements MediaScannerConnectionClient {

	private MediaScannerConnection mMs;

	private String mPath;

	public SingleMediaScanner(Context context, String f) {
		mPath = f;
		mMs = new MediaScannerConnection(context, this);
		mMs.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		mMs.scanFile(mPath, null);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		mMs.disconnect();
	}

}
