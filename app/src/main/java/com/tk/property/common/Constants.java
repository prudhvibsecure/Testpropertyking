package com.tk.property.common;

import android.os.Environment;

public class Constants {
	public static String PATH = Environment.getExternalStorageDirectory()
			.toString();

	public static final String DWLPATH = PATH + "/TPK/downloads/";

	public static final String CACHEPATH = PATH + "/TPK/cache/";

	public static final String CACHEDATA = CACHEPATH + "data/";

	public static final String CACHETEMP = CACHEPATH + "temp/";

	public static final String CACHEIMAGE = CACHEPATH + "images/";

	public static final String GOOGLE_PROJECT_ID = "949836916730";

}
