package india.lg.intern.fit;

/**
 * Created by WooYong on 2017-07-14.
 */

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * Writes/reads an object to/from a private local file
 *
 *
 */
public class DataAccessor {


    private static String filename = "FP_DB.db";

    /**
     *
     * @param context
     * @param object
     */
    public static void appendFp(Context context, Footprint object) {

        ObjectOutputStream objectOut = null;
        try {
            if (object == null) {
                FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
                objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(new ArrayList<Footprint>());
                return;
            }

            ArrayList<Footprint> fpList = readFplist(context);
            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);

            objectOut = new ObjectOutputStream(fileOut);

            object.serializePosList();

            fpList.add(object);
            objectOut.writeObject(fpList);
            fileOut.getFD().sync();

        } catch(FileNotFoundException e) {
            try {
                File.createTempFile(filename, null, context.getCacheDir());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }


    /**
     *
     * @param context
     * @return
     */
    public static ArrayList<Footprint> readFplist(Context context) {

        FileInputStream fileIn = null;
        ObjectInputStream objectIn = null;
        ArrayList<Footprint> object = null;
        try {
            fileIn = context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = (ArrayList<Footprint>) objectIn.readObject();

            if (object != null) {
                for (int i = 0; i < object.size(); i++) {
                    object.get(i).deserializePosList();
                }
            }

        } catch(FileNotFoundException e) {
            appendFp(context, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }

        return object;
    }

}