/*
 * Copyright 2015 Red Hat, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 */

import build.tools.tzdb.TzdbZoneRulesCompiler;
import junit.framework.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Locale;

/**
 * Test for generation of tzdb.dat
 */
public class TzdbGenTest {

    private static final String[] ZONES = new String[]{"africa", "antarctica", "asia", "australasia", "europe",
            "northamerica", "southamerica", "backward", "etcetera", "VERSION"};

    @Test
    public void dummy() {
        // to enable tests comment "System.exit" callse in TzdbZoneRulesCompiler
        // and uncomment test method annotation
    }

//    @Test
    public void test() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        Assert.assertEquals("2014j fail", "be6d80ad33404b71472c9ebb9582d958f53a57f4", sha1SumForVersion("2014j"));
        Assert.assertEquals("2015a fail", "8abcae52ff2c283e61c29980e7f3cb43543392d1", sha1SumForVersion("2015a"));
        Assert.assertEquals("2015b fail", "ec50e9d37192f87743fa16d1c8d4b0101a7789de", sha1SumForVersion("2015b"));
        Assert.assertEquals("2015c fail", "b368182cef7a38cef65213e3535f881960354633", sha1SumForVersion("2015c"));
    }

    private String sha1SumForVersion(String version) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        File tmpdir = null;
        try {
            tmpdir = createTempDir();
            copyZones(version, tmpdir);
            File result = new File(tmpdir, "tzdata.db");
            TzdbZoneRulesCompiler.main(new String[]{
                    "-srcdir", tmpdir.getAbsolutePath(),
                    "-dstfile", result.getAbsolutePath(),
                    "-verbose"
            });
            return sha1SumFromFile(result);
        } finally {
            deleteRecursive(tmpdir);
        }
    }

    private void copyZones(String version, File tmpdir) {
        for (String na : ZONES) {
            String path = "/" + version + "/" + na;
            File target = new File(tmpdir, na);
            copyResource(path, target);
        }
    }

    private void copyResource(String path, File outfile) {
        InputStream is = null;
        OutputStream os = null;
        byte[] buf = new byte[4096];
        try {
            is = TzdbGenTest.class.getResourceAsStream(path);
            os = new FileOutputStream(outfile);
            int read = 0;
            while ((read = is.read(buf)) != -1) {
                os.write(buf, 0, read);
            }
            os.close();
        } catch (Exception e) {
            closeQuietly(os);
        } finally {
            closeQuietly(is);
        }
    }

    private String sha1SumFromFile(File file) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        FileInputStream fis = null;
        byte[] data;
        try {
            data = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            int res = fis.read(data);
            if (res != data.length) throw new IOException(
                    "File read error, expected: [" + data.length + "], read: [" + res +"]");
        } finally {
            closeQuietly(fis);
        }
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1", "SUN");
        byte[] hash = sha1.digest(data);
        return DatatypeConverter.printHexBinary(hash).toLowerCase(Locale.ENGLISH);
    }

    // test-only method, generally unsafe
    private File createTempDir() throws IOException {
        File fi = File.createTempFile(getClass().getName(), ".tmp");
        boolean res = fi.delete();
        if (!res) throw new IOException("Temp dir error");
        boolean resd = fi.mkdir();
        if (!resd) throw new IOException("Temp dir error");
        return fi;
    }

    private void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // test-only method, generally unsafe
    private static void deleteRecursive(File path) throws FileNotFoundException{
        if (null == path) return;
        if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
        if (path.isDirectory()){
            File[] list = path.listFiles();
            for (File f : list){
                deleteRecursive(f);
            }
        }
        path.delete();
    }
}
