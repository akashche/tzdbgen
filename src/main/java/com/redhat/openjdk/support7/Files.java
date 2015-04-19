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

    public static boolean isDirectory(Path path) {
        return path.getFile().isDirectory();
    }

    public static boolean exists(Path path) {
        return path.getFile().exists();
    }

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

    public static OutputStream newOutputStream(Path path) throws FileNotFoundException {
        return new FileOutputStream(path.getFile());
    }

    public static List<String> readAllLines(Path path, Charset charset) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path.getFile());
            Reader fileReader = new InputStreamReader(fis, charset);
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
