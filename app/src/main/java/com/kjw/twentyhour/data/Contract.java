package com.kjw.twentyhour.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {


    private Contract() {}


    public static final String CONTENT_AUTHORITY = "com.example.joongwonkim.somulbo2";


    public  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static String PATH_SOMULBO2 = "somulbo2";


    public static final class SoMulBoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SOMULBO2);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOMULBO2;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOMULBO2;

        public final static String TABLE_NAME = "somulbo";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SMB_NAME = "name";

        public final static String COLUMN_SMB_POSITION = "position";

        public final static String COLUMN_SMB_DATE = "date";

    }

}
