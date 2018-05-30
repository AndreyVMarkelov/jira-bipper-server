package ut.ru.andreymarkelov.atlas.plugins.jirabipperserver;

import org.junit.Test;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.api.MyPluginComponent;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}