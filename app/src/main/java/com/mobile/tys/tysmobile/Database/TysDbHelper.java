package com.mobile.tys.tysmobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobile.tys.tysmobile.Model.OrdenesTransporteContract;
import com.mobile.tys.tysmobile.Model.UsuarioReaderContract;

public class TysDbHelper  extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tys.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UsuarioReaderContract.TABLE_NAME + " ("
                    + UsuarioReaderContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + UsuarioReaderContract.ID + " TEXT NOT NULL,"
                    + UsuarioReaderContract.NAME + " TEXT NOT NULL,"
                    + UsuarioReaderContract.PASSWORD + " TEXT NOT NULL,"
                    + UsuarioReaderContract.EMAIL + " TEXT NOT NULL,"
                    + "UNIQUE (" + UsuarioReaderContract.ID + "))";

    private static final String SQL_CREATE_ENTRIES_ORDENES =
            "CREATE TABLE " + OrdenesTransporteContract.TABLE_NAME + " ("
                    + OrdenesTransporteContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + OrdenesTransporteContract.ID + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.IDESTADO + " INTEGER NOT NULL,"
                    + OrdenesTransporteContract.CLIENTE + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.DESTINO + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.ESTADO + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.ORIGEN + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.NUMCP + " TEXT NOT NULL,"
                    + OrdenesTransporteContract.FECHA + " DATE NOT NULL,"
                    + OrdenesTransporteContract.DNI + " TEXT ,"
                    + OrdenesTransporteContract.NOMBREENTREGA + " TEXT ,"
                    + OrdenesTransporteContract.TIPOENTREGA + " TEXT ,"
                    + OrdenesTransporteContract.FECHAENTREGA + " DATE ,"
                    + OrdenesTransporteContract.HORAENTREGA + " TEXT ,"
                    + OrdenesTransporteContract.OBSERVACION + " TEXT ,"
                    + OrdenesTransporteContract.FILE + " TEXT ,"
                    + "UNIQUE (" + OrdenesTransporteContract.ID + "))";





    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UsuarioReaderContract.TABLE_NAME;


    private static final String SQL_DELETE_ENTRIES_ORDENES =
            "DROP TABLE IF EXISTS " + OrdenesTransporteContract.TABLE_NAME;

    public TysDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES_ORDENES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_ORDENES);
        onCreate(db);

    }
}
