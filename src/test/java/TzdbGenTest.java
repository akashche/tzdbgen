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
        //und uncomment test method annotation
    }

//    @Test
    public void test() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        Assert.assertEquals("2014j fail", "445a3802c5386e621943975d9bdbd7e57194c03b", sha1SumForVersion("2014j"));
        Assert.assertEquals("2015a fail", "610cd4b67e1d91efd92cfd02817b6ffee45a1882", sha1SumForVersion("2015a"));
        Assert.assertEquals("2015b fail", "4cb0510cf411f1d182c539ea7bd5b094e5bbd555", sha1SumForVersion("2015b"));
        Assert.assertEquals("2015c fail", "603b4b75b7d40fd54d20e20d647263cc2aff42c9", sha1SumForVersion("2015c"));
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
