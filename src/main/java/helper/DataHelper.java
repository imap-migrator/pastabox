package helper;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.*;

/**
 * Created by Oliver on 7/12/2017.
 */
public class DataHelper {
    private static HashMap<Class, Logger> loggerHashMap;
    public static File baseDir;
    public static File dataDir;
    public static File logsDir;
    public static boolean licenseAccepted = false;

    public DataHelper() throws UnsupportedEncodingException {
        loggerHashMap = new HashMap<>();

        baseDir = new File(URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8"));

        dataDir = new File(baseDir.getAbsolutePath() + File.separator + "pastabox");
        if(!dataDir.exists())
            dataDir.mkdir();

        logsDir = new File(dataDir.getAbsolutePath() + File.separator + "logs");
        if(!logsDir.exists())
            logsDir.mkdir();
    }

    public static Logger getLogger(Class context) {
        if(!loggerHashMap.containsKey(context)) {
            Logger logger = Logger.getLogger(context.getName());
            logger.setLevel(Level.ALL);

            // Try to write the log to a file. If this fails, it'll fallback to console.
            try {
                SimpleFormatter formatter = new SimpleFormatter();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                Handler handler = new FileHandler(logsDir.getAbsolutePath() + File.separator + dateFormat.format(date) +  ".txt", true);
                handler.setFormatter(formatter);
                logger.addHandler(handler);
            } catch (Throwable e) {
                Logger.getLogger(DataHelper.class.getName()).log(Level.WARNING, "Could not write to log file", e);
            }
            loggerHashMap.put(context, logger);
        }
        return loggerHashMap.get(context);
    }

    public static void setLicenseAccepted(boolean licenseAccepted) {
        DataHelper.licenseAccepted = licenseAccepted;
    }

    public static boolean isLicenseAccepted() {
        return DataHelper.licenseAccepted;
    }
}
