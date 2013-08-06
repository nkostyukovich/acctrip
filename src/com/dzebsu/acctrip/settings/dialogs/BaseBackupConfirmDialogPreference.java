package com.dzebsu.acctrip.settings.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;

import com.dzebsu.acctrip.db.EventAccDbHelper;
import com.dzebsu.acctrip.settings.SingleMediaScanner;

public abstract class BaseBackupConfirmDialogPreference extends BaseDialogPreference {

	protected static final String PACKAGE_NAME = "com.dzebsu.acctrip";

	protected static final String DATABASE_NAME = EventAccDbHelper.DATABASE_NAME;

	public BaseBackupConfirmDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	protected String getDBLocation() {
		return String.format("//data//%s//databases//%s", PACKAGE_NAME, DATABASE_NAME);
	}

	protected String makeBackupDBToDeviceExternalMemory(boolean inverse) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite() && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				String currentDBPath = getDBLocation();
				String backupDBPath = DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				FileInputStream fis = null;
				FileOutputStream fos = null;
				FileChannel src = null;
				FileChannel dst = null;
				try {
					if (!inverse) {
						if (currentDB.exists()) {
							fis = new FileInputStream(currentDB);
							fos = new FileOutputStream(backupDB);
							src = fis.getChannel();
							dst = fos.getChannel();
							dst.transferFrom(src, 0, src.size());
							return backupDB.getPath();
						}
					} else {
						fis = new FileInputStream(backupDB);
						fos = new FileOutputStream(currentDB);
						src = fis.getChannel();
						dst = fos.getChannel();
						dst.transferFrom(src, 0, src.size());
						return currentDB.getPath();
					}
				} finally {
					src.close();
					dst.close();
					fis.close();
					fos.close();
					new SingleMediaScanner(cxt, backupDB.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		return null;
	}

}
