// Copyright 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.JavascriptStack;

import java.util.List;
import java.util.Locale;

/**
 * The default Javascript Stack consists of Prototype, Scriptaculous & the Tapestry-specific library.
 *
 * @since 5.1.0.2
 */
public class JavascriptStackImpl implements JavascriptStack
{
    private final SymbolSource symbolSource;

    private final AssetSource assetSource;

    private final ThreadLocale threadLocale;

    private final List<Asset> coreResources = CollectionFactory.newList();

    private static final String[] CORE_RESOURCES = new String[]
            {
                    // Core scripts added to any page that uses scripting

                    "${tapestry.scriptaculous}/prototype.js",
                    "${tapestry.scriptaculous}/scriptaculous.js",
                    "${tapestry.scriptaculous}/effects.js",

                    // Uses functions defined by the prior three

                    "org/apache/tapestry5/tapestry.js"
            };

    public JavascriptStackImpl(SymbolSource symbolSource, AssetSource assetSource, ThreadLocale threadLocale)
    {
        this.symbolSource = symbolSource;
        this.assetSource = assetSource;
        this.threadLocale = threadLocale;

        for (String path : CORE_RESOURCES)
        {
            coreResources.add(expand(path, null));
        }
    }

    private Asset expand(String path, Locale locale)
    {
        String expanded = symbolSource.expandSymbols(path);

        return assetSource.getAsset(null, expanded, locale);
    }

    public List<Asset> getStack()
    {
        List<Asset> result = CollectionFactory.newList(coreResources);

        Asset messages = assetSource.getAsset(null, "org/apache/tapestry5/tapestry-messages.js",
                                              threadLocale.getLocale());

        result.add(messages);

        return result;
    }
}
