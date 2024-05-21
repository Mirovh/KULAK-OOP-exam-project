import com.alchemy.*;
import com.alchemy.IngredientConditions.*;
import com.alchemy.quantity.FluidUnit;
import com.alchemy.quantity.Quantity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static com.alchemy.quantity.PowderUnit.*;
import static org.junit.Assert.*;

public class DeviceTest {
    Laboratory lab;
    Laboratory lab2;
    AlchemicIngredient ingredient;
    AlchemicIngredient ingredient2;
    IngredientContainer container1;
    IngredientContainer container2;
    Quantity basicQuantity = new Quantity(1, FluidUnit.SPOON);


    @Before
    public void setUpFixture() {
        ingredient = new AlchemicIngredient(5F);
        ingredient2 = new AlchemicIngredient(5F);
        container1 = new IngredientContainer(ingredient,basicQuantity.getUnit());
        container2 = new IngredientContainer(ingredient2,basicQuantity.getUnit());
        lab = new Laboratory(10000);
        lab2 = new Laboratory(10000);
    }

    @Test
    public void CoolingBoxTest() throws Exception {
        //adding Ingredients test
        CoolingBox fridge = new CoolingBox();
        CoolingBox fridge2 = new CoolingBox(40F, 0F);
        fridge.setTemperature(20F,0F);
        lab.addDevice(fridge);
        lab2.addDevice(fridge2);
        //test DeviceFullException
        fridge.addIngredient(container1);
        assertThrows(Device.DeviceFullException.class,()-> fridge.addIngredient(container2));
        //test if container gets destroyed
        Assert.assertNull(container1.getContent());
        //test basic cooling function
        fridge.react();
        IngredientContainer cooledContainer = fridge.getContents();
        Assert.assertEquals(20F,cooledContainer.getContent().getTemperature().getColdness(), 1);
        Assert.assertEquals(0F, cooledContainer.getContent().getTemperature().getHotness(), 1);
        fridge2.addIngredient(cooledContainer);
        fridge2.react();
        IngredientContainer cooledContainer2 = fridge2.getContents();
        Assert.assertEquals(40F, cooledContainer2.getContent().getTemperature().getColdness(), 1);
        Assert.assertEquals(0F, cooledContainer2.getContent().getTemperature().getHotness(), 1);
        // test ingredient doesn't heat up when in coolingBox
        fridge.addIngredient(cooledContainer2);
        fridge.react();
        IngredientContainer cooledContainer3 = fridge.getContents();
        Assert.assertEquals(40F, cooledContainer3.getContent().getTemperature().getColdness(), 1);
        Assert.assertEquals(0F, cooledContainer3.getContent().getTemperature().getHotness(), 1);
    }

    @Test
    public void OvenTest() throws Exception{
        //Test is run 20 times to take into account the randomness
        //adding Ingredients test
        Oven oven = new Oven();
        Oven oven2 = new Oven(0F, 60F);
        oven.setTemperature(0F,40F);
        lab.addDevice(oven);
        lab2.addDevice(oven2);
        //test DeviceFullException
        oven.addIngredient(container1);
        assertThrows(Device.DeviceFullException.class,()-> oven.addIngredient(container2));
        //test if container gets destroyed
        Assert.assertNull(container1.getContent());
        //test basic heating function
        oven.react();
        IngredientContainer heatedContainer = oven.getContents();
        Assert.assertTrue(35<=heatedContainer.getContent().getTemperature().getHotness()&& heatedContainer.getContent().getTemperature().getHotness() <=45);
        Assert.assertEquals(0F, heatedContainer.getContent().getTemperature().getColdness(), 1);
        oven2.addIngredient(heatedContainer);
        oven2.react();
        IngredientContainer heatedContainer2 = oven2.getContents();
        Assert.assertTrue(55<=heatedContainer2.getContent().getTemperature().getHotness()&& heatedContainer2.getContent().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0F, heatedContainer2.getContent().getTemperature().getColdness(), 1);
        // test ingredient doesn't cool down when in Oven
        oven.addIngredient(heatedContainer2);
        oven.react();
        IngredientContainer heatedContainer3 = oven.getContents();
        Assert.assertTrue(55<=heatedContainer3.getContent().getTemperature().getHotness()&& heatedContainer3.getContent().getTemperature().getHotness()<=65 );
        Assert.assertEquals(0F, heatedContainer3.getContent().getTemperature().getColdness(), 1);
    }

    @Test
    public void TransmorgrifierTest() throws Exception{
       Transmorgrifier transmorgrifier = new Transmorgrifier();
       lab.addDevice(transmorgrifier);
       Assert.assertFalse(container1.getContent().getState().getState().isSolid());
       //test basic function
       transmorgrifier.addIngredient(container1);
       transmorgrifier.react();
       IngredientContainer changedContainer = transmorgrifier.getContents();
       Assert.assertTrue(changedContainer.getContent().getState().getState().isSolid());
       transmorgrifier.addIngredient(changedContainer);
       transmorgrifier.react();
        IngredientContainer changedContainer2 = transmorgrifier.getContents();
        Assert.assertFalse(changedContainer2.getContent().getState().getState().isSolid());
    }

    @Test
    public void KettleTestName() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //mixing of two ingredients with same name
        kettle.addIngredient(container1);
        kettle.addIngredient(container2);
        kettle.react();
        assertEquals(kettle.getContents().getContent().getFullName(), ingredient.getFullName());
        //mixing multiple ingredients with different names
        Temperature basicTemp = new Temperature(0,20);
        IngredientState.State bassicState = IngredientState.State.Liquid;
        int basicAmount = 15;
        AlchemicIngredient testIngredient1 = new AlchemicIngredient("Name One",basicTemp,bassicState,basicAmount);
        AlchemicIngredient testIngredient2 = new AlchemicIngredient("Name Two",basicTemp,bassicState,basicAmount);
        AlchemicIngredient testIngredient3 = new AlchemicIngredient("Name Three",basicTemp,bassicState,basicAmount);
        kettle.addIngredient(new IngredientContainer(testIngredient1,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient3,FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient testIngredient4 = kettle.getContents().getContent();
        Assert.assertEquals(testIngredient4.getFullName(),"Name One mixed with Name Three mixed with Name Two");
        AlchemicIngredient testIngredient5 = new AlchemicIngredient("An Alphabetically First Name",basicTemp,bassicState,basicAmount);
        kettle.addIngredient(new IngredientContainer(testIngredient4,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient5,FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient testIngredient6 = kettle.getContents().getContent();
        Assert.assertEquals(testIngredient6.getFullName(),"An Alphabetically First Name mixed with Name One mixed with Name Three mixed with Name Two");
    }
    @Test
    public void KettleTestStateAndStandardTemperature() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //mixing one ingredient close to [0,20] and one far away
        AlchemicIngredient closeTo20 = new AlchemicIngredient("Name",new Temperature(0,19), IngredientState.State.Powder,5);
        AlchemicIngredient farFrom20 = new AlchemicIngredient("Othername",new Temperature(50,0), IngredientState.State.Liquid,5);
        kettle.addIngredient(new IngredientContainer(closeTo20,CHEST));
        kettle.addIngredient(new IngredientContainer(farFrom20, FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient newIngredient = kettle.getContents().getContent();
        Assert.assertEquals(newIngredient.getState().isSolid(),closeTo20.getState().isSolid());
        Assert.assertEquals(newIngredient.getStandardType().getStandardTemperature().getTemperature(),closeTo20.getStandardType().getStandardTemperature().getTemperature());
        //mixing two ingredients equally far from [0,20] with different states and standardTemperatures
        AlchemicIngredient equalTo20 = new AlchemicIngredient("Yet Another Name",new Temperature(0,21), IngredientState.State.Powder,5);
        kettle.addIngredient(new IngredientContainer(closeTo20,CHEST));
        kettle.addIngredient(new IngredientContainer(equalTo20, CHEST));
        kettle.react();
        AlchemicIngredient newIngredient2 = kettle.getContents().getContent();
        Assert.assertEquals(newIngredient2.getState().isSolid(),equalTo20.getState().isSolid());
        Assert.assertEquals(newIngredient2.getStandardType().getStandardTemperature(),equalTo20.getStandardType().getStandardTemperature());
    }
    @Test
    public void kettleTestQuantity() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        //testing basic addition of ingredients of same state
        AlchemicIngredient testIngredient1 = new AlchemicIngredient(new Quantity(5, FluidUnit.SPOON));
        AlchemicIngredient testIngredient2 = new AlchemicIngredient(new Quantity(3,FluidUnit.JUG));
        kettle.addIngredient(new IngredientContainer(testIngredient1, FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2, FluidUnit.BARREL));
        kettle.react();
        AlchemicIngredient newIngredient = kettle.getContents().getContent();
        assertTrue(newIngredient.getQuantity().isEqualTo(new Quantity(320,FluidUnit.SPOON)));
        //testing rounding down of units smaller than spoons of opposite state
        kettle.addIngredient(new IngredientContainer(newIngredient,FluidUnit.BARREL));
        AlchemicIngredient smallIngredient1 = new AlchemicIngredient("Small",new Temperature(0,30), new IngredientState(true),new Quantity(5,PINCH));
        AlchemicIngredient smallIngredient2 = new AlchemicIngredient("Smaller",new Temperature(0,30),new IngredientState(true),new Quantity(4,PINCH));
        kettle.addIngredient(new IngredientContainer(testIngredient1, FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(smallIngredient1, CHEST));
        kettle.addIngredient(new IngredientContainer(smallIngredient2,CHEST));
        kettle.react();
        newIngredient = kettle.getContents().getContent();
        assertTrue(newIngredient.getQuantity().isEqualTo(new Quantity(326,FluidUnit.SPOON)));
    }
    @Test
    public void KettleTestTemperature() throws Exception{
        Kettle kettle = new Kettle();
        lab.addDevice(kettle);
        AlchemicIngredient testIngredient1 = new AlchemicIngredient("Name",new Temperature(50,0),new IngredientState(false),new Quantity(5,FluidUnit.SPOON));
        AlchemicIngredient testIngredient2 = new AlchemicIngredient("New Name",new Temperature(0,30),new IngredientState(false),new Quantity(3,FluidUnit.SPOON));
        kettle.addIngredient(new IngredientContainer(testIngredient1,FluidUnit.BARREL));
        kettle.addIngredient(new IngredientContainer(testIngredient2,FluidUnit.BARREL));
        kettle.react();
        IngredientContainer mixedContainer = kettle.getContents();
        float expectedValue =((float) (5 * (-50)) /8)+((float) (3 * (30)) /8);
        assertTrue(mixedContainer.getContent().getTemperature().getHotness() == expectedValue | mixedContainer.getContent().getTemperature().getColdness() == expectedValue |mixedContainer.getContent().getTemperature().getHotness() == -expectedValue | mixedContainer.getContent().getTemperature().getColdness() == -expectedValue );
    }
}
