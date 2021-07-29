package dev.lightdream.ultraprisoncore.gangs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public FileUtils() {
    }

    public static void copy(InputStream var0, File var1) {
        try {
            FileOutputStream var2 = new FileOutputStream(var1);
            byte[] var3 = new byte[1024];

            int var4;
            while((var4 = var0.read(var3)) > 0) {
                var2.write(var3, 0, var4);
            }

            var2.close();
            var0.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static void mkdir(File var0) {
        try {
            var0.mkdir();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
