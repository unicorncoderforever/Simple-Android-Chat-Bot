package aimlwriter;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;


import org.alicebot.ab.MagicStrings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by preethirao on 2/21/17.
 */

public class FileWriter extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FileWriter";
    Context mContext;
    String root_dir_application;
    String botName = "MY_BOT";
    final static String AIML = "/Super";
    IWritinngCompletionCallback mCallback;

    public interface IWritinngCompletionCallback {
        void onWritingComplete();
    }

    public FileWriter(Context context, IWritinngCompletionCallback callback) {
        mContext = context;
        root_dir_application = context.getFilesDir() + AIML + File.separator + "bots" + File.separator + botName;
        mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AssetManager assets = mContext.getResources().getAssets();
        File jayDir = new File(root_dir_application);
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            try {
                for (String dir : assets.list("super")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    subdir.mkdirs();
                    for (String file : assets.list("super/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
//                        if (f.exists()) {
//                            continue;
//                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("super/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "does not exist");
        }
        return null;

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MagicStrings.root_path = mContext.getFilesDir() + AIML;
        mCallback.onWritingComplete();
    }
}
