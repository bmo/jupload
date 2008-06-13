//
// $Id$
//
// jupload - A file upload applet.
//
// Copyright 2007 The JUpload Team
//
// Created: 08.06.2007
// Creator: felfert
// Last modified: $Date$
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

package wjhk.jupload2.upload;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements a container for multiple cookies in a single domain.
 * 
 * @author felfert
 */
public class CookieJar {

    final static Pattern pNvPair = Pattern.compile(
            "^\\s*([^=\\s]+)(\\s*=\\s*(.+))*$", Pattern.CASE_INSENSITIVE);

    private HashMap<String, Cookie> jar = new HashMap<String, Cookie>();

    private String domain = null;

    private class Cookie implements Cloneable {

        String domain = null;

        String name = null;

        String value = null;

        String path = null;

        long max_age = 0;

        int version = 0;

        int secure = 0;

        Cookie() {
            //
        }

        /**
         * @throws CloneNotSupportedException
         * @see java.lang.Object#clone()
         */
        @Override
        public Cookie clone() throws CloneNotSupportedException {
            Cookie ret = (Cookie) super.clone();
            ret.domain = this.domain;
            ret.name = this.name;
            ret.value = this.value;
            ret.path = this.path;
            ret.max_age = this.max_age;
            ret.version = this.version;
            ret.secure = this.secure;
            return ret;
        }

        /**
         * Retrieves the hash value of this cookie. Cookies are hashed by name
         * and path.
         * 
         * @return The hash value of this cookie.
         */
        public String getKey() {
            String ret = this.name;
            if (null != this.path)
                ret += this.path;
            return ret;
        }

        /**
         * Returns a single client cookie header element
         * 
         * @param path The path of the corresponding request URI
         * @param secure 1, if the current connection is secure (SSL), 0
         *            otherwise
         * @return The part of the cookie header or an empty string
         */
        public String getHeader(String path, int secure) {
            StringBuffer sb = new StringBuffer();
            if ((null == this.path || this.path.equals("/") || this.path
                    .startsWith(path))
                    && (this.secure <= secure)
                    && (this.max_age > System.currentTimeMillis())) {
                if (this.version > 0) {
                    sb.append("$Version=").append(this.version).append("; ");
                    sb.append(this.name).append("=").append(this.value).append(
                            ";");
                    if (null != this.path)
                        sb.append(" $Path=").append(this.path).append(";");
                    if (null != this.domain)
                        sb.append(" $Domain=").append(this.domain).append(";");
                } else {
                    sb.append(this.name).append("=").append(this.value).append(
                            ";");
                }
            }
            return sb.toString();
        }

    }

    private String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\""))
            return s.substring(1, s.length() - 1);
        return s;
    }

    private boolean domainMatch(String cd) {
        if (!cd.startsWith(".")) {
            int dot = cd.indexOf('.');
            if (dot >= 0)
                cd = cd.substring(dot);

        }
        return cd.equals(this.domain);
    }

    /**
     * Sets the domain for this cookie jar. If set, only cookies matching the
     * specified domain are handled.
     * 
     * @param domain The domain of this instance
     */
    public void setDomain(String domain) {
        if (!domain.startsWith(".")) {
            int dot = domain.indexOf('.');
            if (dot >= 0)
                domain = domain.substring(dot);

        }
        this.domain = domain;
    }

    /**
     * Builds a RFC 2109 compliant client cookie header for the specified URL.
     * 
     * @param url The URL for which the cookie header is to be used.
     * @return A client cookie header (including the "Cookie: " prefix) or null
     *         if no cookies are to be set.
     */
    public String buildCookieHeader(URL url) {
        String domain = url.getHost();
        int dot = domain.indexOf('.');
        if (dot >= 0)
            domain = domain.substring(dot);
        if (domain.equals(this.domain)) {
            String path = url.getPath();
            int secure = url.getProtocol().equalsIgnoreCase("https") ? 1 : 0;
            StringBuffer sb = new StringBuffer();
            for (String key : this.jar.keySet()) {
                Cookie c = this.jar.get(key);
                if (null != c)
                    sb.append(c.getHeader(path, secure));
            }
            if (sb.length() > 0) {
                sb.append("\r\n");
                return "Cookie: " + sb.toString();
            }
        }
        return null;
    }

    /**
     * Parses a "Set-Cookie" header and creates/updates/deletes cookies
     * according to the parsed values. Parsing is done according to the
     * specification in RFC 2109
     * 
     * @param s The plain value of the "Set-Cookie" HTTP header. e.g.: without
     *            the "Set-Cookie: " prefix.
     */
    public void parseCookieHeader(String s) {
        StringTokenizer t = new StringTokenizer(s, ";");
        Cookie cookie = new Cookie();
        while (t.hasMoreTokens()) {
            Matcher m = pNvPair.matcher(t.nextToken());
            if (m.matches()) {
                String n = m.group(1);
                String v = (m.groupCount() > 2) ? m.group(3).trim() : "";
                if (n.compareToIgnoreCase("version") == 0) {
                    cookie.version = Integer.parseInt(v);
                    continue;
                }
                if (n.compareToIgnoreCase("domain") == 0) {
                    cookie.domain = v;
                    continue;
                }
                if (n.compareToIgnoreCase("path") == 0) {
                    cookie.path = v;
                    continue;
                }
                if (n.compareToIgnoreCase("max-age") == 0) {
                    cookie.max_age = Integer.parseInt(v);
                    if (cookie.max_age < 0)
                        cookie.max_age = 0;
                    cookie.max_age *= 1000;
                    cookie.max_age += System.currentTimeMillis();
                    continue;
                }
                if (n.compareToIgnoreCase("expires") == 0) {
                    SimpleDateFormat df = new SimpleDateFormat(
                            "EEE, dd-MMM-yy HH:mm:ss zzz");
                    try {
                        cookie.max_age = System.currentTimeMillis()
                                - df.parse(v).getTime();
                        if (cookie.max_age < 0)
                            cookie.max_age = 0;
                    } catch (ParseException e) {
                        cookie.max_age = 0;
                    }
                    continue;
                }
                if (n.compareToIgnoreCase("comment") == 0) {
                    // ignored
                    continue;
                }
                if (n.compareToIgnoreCase("secure") == 0) {
                    cookie.secure = 1;
                    continue;
                }
                if (!n.startsWith("$")) {
                    if (null != cookie.name) {
                        if (cookie.version > 0) {
                            // Strip possible quotes
                            cookie.domain = stripQuotes(cookie.domain);
                            cookie.path = stripQuotes(cookie.path);
                            // cookie.comment = stripQuotes(cookie.comment);
                            // cookie.name = stripQuotes(cookie.name);
                            // cookie.value = stripQuotes(cookie.value);
                        }
                        if (domainMatch(cookie.domain)) {
                            if (cookie.max_age > 0)
                                this.jar.put(cookie.getKey(), cookie);
                            else
                                this.jar.put(cookie.getKey(), null);
                        }
                        try {
                            cookie = cookie.clone();
                        } catch (CloneNotSupportedException e) {
                            cookie = new Cookie();
                        }
                    }
                    cookie.name = n;
                    cookie.value = v;
                }
            }
        }
    }
}
