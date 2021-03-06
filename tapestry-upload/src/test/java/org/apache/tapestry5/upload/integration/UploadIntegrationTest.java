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

package org.apache.tapestry5.upload.integration;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.example.upload.pages.Start;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class UploadIntegrationTest extends SeleniumTestCase
{

    @BeforeTest
    public void setupTargetFolder() throws IOException
    {
        File target = new File(Start.TARGET_DIR);
        if (!target.exists())
        {
            target.mkdirs();
        } else
        {
            for (File file : target.listFiles())
            {
                file.delete();
            }
        }
    }

    @Test(enabled = false)
    public void integration_test() throws Exception
    {

        openBaseURL();

        File source = new File("test/data/upload.txt");

        type("name", "Sample file");
        type("file", source.getCanonicalPath());

        clickAndWait("//input[@value='Upload']");
    }
}
