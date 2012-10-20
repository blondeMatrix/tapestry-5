// Copyright 2012 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.javascript.InitializationPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleInitsManager
{
    private final Set<String> pureInits = CollectionFactory.newSet();

    private final Map<InitializationPriority, List<JSONArray>> inits = CollectionFactory.newMap();

    private int initCount;

    public void addInitialization(InitializationPriority priority, String moduleName, String functionName, JSONArray arguments)
    {
        assert priority != null;
        assert InternalUtils.isNonBlank(moduleName);

        String name = functionName == null ? moduleName : moduleName + ":" + functionName;

        if ((arguments == null || arguments.length() == 0))
        {
            if (pureInits.contains(name))
            {
                // A degenerate case is a pure init added repeatedly with different priorities. That isn't handled:
                // the first priority wins.
                return;
            }

            pureInits.add(name);
        }

        JSONArray init = new JSONArray();

        init.put(name);

        init.putAll(arguments);

        InternalUtils.addToMapList(inits, priority, init);

        initCount++;
    }

    public List<JSONArray> forPriority(InitializationPriority... priorities)
    {
        List<JSONArray> result = new ArrayList<JSONArray>(initCount);

        for (InitializationPriority p : priorities)
        {
            List<JSONArray> initsForPriority = inits.get(p);

            if (initsForPriority != null)
            {
                result.addAll(initsForPriority);
            }
        }

        return result;
    }
}