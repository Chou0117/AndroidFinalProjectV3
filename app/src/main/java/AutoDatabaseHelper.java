import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by Lewis User on 2017-12-18.
 */

public class AutoDatabaseHelper extends SQLiteOpenHelper {

    private static final String ACTIVITY_NAME = "AutoDatabaseHelper";

    private static final String DATABASE_NAME = "Automobile.db";
    private static final int VERSION_NUM = 1;
    private static final String AUTO_TABLE = "Auto_Table";
    //PRIMARY KEY
    private static final String PRIMARY_KEY = "_ID";
    private static final String COLUMN_LITRES = "LITRES";
    private static final String COLUMN_PRICE = "PRICE";
    private static final String COLUMN_MILEAGE = "MILEAGE";

    public static final String[] Column_Names = new String[]{
            PRIMARY_KEY,
            COLUMN_LITRES,
            COLUMN_PRICE,
            COLUMN_MILEAGE
    };

    public AutoDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

}
    private static final String CREATE_AUTO_TABLE =
            "create table "  + AUTO_TABLE  + " ( " + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LITRES + " numeric " + COLUMN_PRICE + " numeric " + COLUMN_MILEAGE + " numeric ); " ;

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("AutoDatabaseHelper", "Calling onCreate");
        Log.i(ACTIVITY_NAME, CREATE_AUTO_TABLE);
        db.execSQL(CREATE_AUTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("AutoDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + "newVersion="+ newVer);
        db.execSQL("DROP TABLE IF EXISTS " + AUTO_TABLE);
        onCreate(db);

    }
}
