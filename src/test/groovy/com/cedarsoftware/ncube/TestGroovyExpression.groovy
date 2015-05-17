package com.cedarsoftware.ncube

import org.junit.Assert
import org.junit.Test

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br/>
 *         Copyright (c) Cedar Software LLC
 *         <br/><br/>
 *         Licensed under the Apache License, Version 2.0 (the 'License')
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br/><br/>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br/><br/>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an 'AS IS' BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
class TestGroovyExpression
{
    @Test
    void testDefaultConstructorIsPrivateForSerialization()
    {
        Class c = GroovyExpression.class
        Constructor<GroovyExpression> con = c.getDeclaredConstructor()
        Assert.assertEquals Modifier.PRIVATE, con.modifiers & Modifier.PRIVATE
        con.accessible = true
        Assert.assertNotNull con.newInstance()
    }

    @Test
    void testCompilerErrorOutput()
    {
        NCube ncube = NCubeManager.getNCubeFromResource("GroovyExpCompileError.json")
        Map coord = [state:'OH']
        Object x = ncube.getCell(coord)
        assert 'Hello, Ohio' == x
        coord.state = 'TX'
        try
        {
            ncube.getCell(coord)
            fail();
        }
        catch (RuntimeException e)
        {
            String msg = e.getCause().getCause().message
            assert msg.toLowerCase().contains('no such property')
            assert msg.toLowerCase().contains('hi8')
        }
    }
}
