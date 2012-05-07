/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.janus.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Flash {
    private Map<String, Object> entries;

    private Flash() {
        entries = new HashMap<String, Object>();
    }

    public void put(String key, Object value) {
        entries.put(key, value);
    }

    public Object get(String key) {
        return entries.get(key);
    }

    public <T> T get(String key, Class<T> exp) {
        T result = (T) entries.get(key);
        entries.remove(key);
        return result;
    }

    public void clear() {
        entries.clear();
    }
    
    public static Flash getForRequest(HttpServletRequest req) {
        HttpSession session = req.getSession();

        Flash flash = (Flash) session.getAttribute(SessionKeys.FLASH);

        if (flash == null) {
            flash = new Flash();
            session.setAttribute(SessionKeys.FLASH, flash);
        }

        return flash;
    }
}
