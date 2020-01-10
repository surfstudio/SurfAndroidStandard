/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.filestorage.naming;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.surfstudio.android.logger.Logger;

public class Sha256NamingProcessor implements NamingProcessor {

    private static final String ALGORITHM = "SHA-256";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HEX_FORMAT = "%064x";

    @Override
    public String getNameFrom(@NotNull String rawName) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
            md.update(rawName.getBytes(CHARSET_NAME));
            byte[] digest = md.digest();
            return String.format(HEX_FORMAT, new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return "";
    }
}
