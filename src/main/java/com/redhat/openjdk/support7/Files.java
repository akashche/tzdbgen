/*
 * Copyright 2015 Red Hat, Inc.
 *
 * This file is part of tzdbgen.
 *
 * tzdbgen is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2, or (at your
 * option) any later version.
 *
 * tzdbgen is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with tzdbgen; see the file COPYING.  If not see
 * <http://www.gnu.org/licenses/>.
 *
 * Linking this code with other modules is making a combined work
 * based on this code.  Thus, the terms and conditions of the GNU
 * General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this code give
 * you permission to link this code with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also
 * meet, for each linked independent module, the terms and conditions
 * of the license of that module.  An independent module is a module
 * which is not derived from or based on this code.  If you modify
 * this code, you may extend this exception to your version of the
 * library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */

package com.redhat.openjdk.support7;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Partial emulation of NIO.2 class
 */
public class Files {

    /**
     * Tests whether a file is a directory.
     *
     * @param   path
     *          the path to the file to test
     *
     * @return  {@code true} if the file is a directory; {@code false} if
     *          the file does not exist, is not a directory, or it cannot
     *          be determined if the file is a directory or not.
     *
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, its {@link SecurityManager#checkRead(String) checkRead}
     *          method denies read access to the file.
     */
    public static boolean isDirectory(Path path) {
        return path.getFile().isDirectory();
    }

    /**
     * Tests whether a file exists.
     *
     * <p> Note that the result of this method is immediately outdated. If this
     * method indicates the file exists then there is no guarantee that a
     * subsequence access will succeed. Care should be taken when using this
     * method in security sensitive applications.
     *
     * @param   path
     *          the path to the file to test
     * .
     * @return  {@code true} if the file exists; {@code false} if the file does
     *          not exist or its existence cannot be determined.
     *
     * @throws  SecurityException
     *          In the case of the default provider, the {@link
     *          SecurityManager#checkRead(String)} is invoked to check
     *          read access to the file.
     *
     */
    public static boolean exists(Path path) {
        return path.getFile().exists();
    }

    /**
     * Reads all the bytes from a file. The method ensures that the file is
     * closed when all bytes have been read or an I/O error, or other runtime
     * exception, is thrown.
     *
     * <p> Note that this method is intended for simple cases where it is
     * convenient to read all bytes into a byte array. It is not intended for
     * reading in large files.
     *
     * @param   path
     *          the path to the file
     *
     * @return  a byte array containing the bytes read from the file
     *
     * @throws  IOException
     *          if an I/O error occurs reading from the stream
     * @throws  OutOfMemoryError
     *          if an array of the required size cannot be allocated, for
     *          example the file is larger that {@code 2GB}
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, the {@link SecurityManager#checkRead(String) checkRead}
     *          method is invoked to check read access to the file.
     */
    public static byte[] readAllBytes(Path path) throws IOException {
        FileInputStream fis = null;
        try {
            byte[] data = new byte[(int) path.getFile().length()];
            fis = new FileInputStream(path.getFile());
            int res = fis.read(data);
            if (res != data.length) throw new IOException(
                    "File read error, expected: [" + data.length + "], read: [" + res +"]");
            return data;
        } finally {
            AutoCloseableUtils.closeQuietly(fis);
        }
    }

    /**
     * Opens or creates a file, returning an output stream that may be used to
     * write bytes to the file. The resulting stream will not be buffered.
     *
     * @param   path
     *          the path to the file to open or create
     *
     * @return  a new output stream
     *
     * @throws  IllegalArgumentException
     *          if {@code options} contains an invalid combination of options
     * @throws  UnsupportedOperationException
     *          if an unsupported option is specified
     * @throws  IOException
     *          if an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, the {@link SecurityManager#checkWrite(String) checkWrite}
     *          method is invoked to check write access to the file.
     */
    public static OutputStream newOutputStream(Path path) throws FileNotFoundException {
        return new FileOutputStream(path.getFile());
    }

    /**
     * Read all lines from a file. This method ensures that the file is
     * closed when all bytes have been read or an I/O error, or other runtime
     * exception, is thrown. Bytes from the file are decoded into characters
     * using the specified charset.
     *
     * <p> This method recognizes the following as line terminators:
     * <ul>
     *   <li> <code>&#92;u000D</code> followed by <code>&#92;u000A</code>,
     *     CARRIAGE RETURN followed by LINE FEED </li>
     *   <li> <code>&#92;u000A</code>, LINE FEED </li>
     *   <li> <code>&#92;u000D</code>, CARRIAGE RETURN </li>
     * </ul>
     * <p> Additional Unicode line terminators may be recognized in future
     * releases.
     *
     * <p> Note that this method is intended for simple cases where it is
     * convenient to read all lines in a single operation. It is not intended
     * for reading in large files.
     *
     * @param   path
     *          the path to the file
     * @param   cs
     *          the charset to use for decoding
     *
     * @return  the lines from the file as a {@code List}; whether the {@code
     *          List} is modifiable or not is implementation dependent and
     *          therefore not specified
     *
     * @throws  IOException
     *          if an I/O error occurs reading from the file or a malformed or
     *          unmappable byte sequence is read
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, the {@link SecurityManager#checkRead(String) checkRead}
     *          method is invoked to check read access to the file.
     */
    public static List<String> readAllLines(Path path, Charset cs) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path.getFile());
            Reader fileReader = new InputStreamReader(fis, cs);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } finally {
            AutoCloseableUtils.closeQuietly(fis);
        }
    }
}
