/*
 * Copyright 2015-2017 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Cristina Yenyxe Gonzalez Garcia <cyenyxe@ebi.ac.uk>
 */
public final class CompressionUtils {

    private CompressionUtils() {
    }

    public static byte[] compress(byte[] data) throws IOException {

        Deflater deflater = new Deflater();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            deflater.setInput(data);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            byte[] output = outputStream.toByteArray();

            Logger.getLogger(CompressionUtils.class.getName()).log(Level.FINE, "Original: {0} Kb", data.length / 1024);
            Logger.getLogger(CompressionUtils.class.getName()).log(Level.FINE, "Compressed: {0} Kb", output.length / 1024);
            return output;
        } finally {
            deflater.end();
        }
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            inflater.setInput(data);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            byte[] output = outputStream.toByteArray();

            Logger.getLogger(CompressionUtils.class.getName()).log(Level.FINE, "Original: {0}", data.length);
            Logger.getLogger(CompressionUtils.class.getName()).log(Level.FINE, "Compressed: {0}", output.length);
            return output;
        } finally {
            inflater.end();
        }
    }
}
