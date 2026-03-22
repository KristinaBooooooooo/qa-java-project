package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для класса Burger.
 * Использую моки Bun и Ingredient, чтобы проверить только логику Burger,
 * не завися от реальных реализаций булок и ингредиентов.
 */
@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    private Burger burger;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockSauce;

    @Mock
    private Ingredient mockFilling;

    @Before
    public void setUp() {
        burger = new Burger();
        when(mockBun.getName()).thenReturn("Mock Bun");
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockSauce.getType()).thenReturn(IngredientType.SAUCE);
        when(mockSauce.getName()).thenReturn("Mock Sauce");
        when(mockSauce.getPrice()).thenReturn(50f);
        when(mockFilling.getType()).thenReturn(IngredientType.FILLING);
        when(mockFilling.getName()).thenReturn("Mock Filling");
        when(mockFilling.getPrice()).thenReturn(75f);
    }

    @Test
    public void setBunsShouldAssignBun() {
        burger.setBuns(mockBun);

        assertSame("Булка должна быть сохранена", mockBun, burger.bun);
    }

    @Test
    public void addIngredientShouldAddIngredientToList() {
        burger.addIngredient(mockSauce);

        assertEquals("Размер списка должен быть 1", 1, burger.ingredients.size());
    }

    @Test
    public void removeIngredientShouldDeleteIngredientByIndex() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.removeIngredient(0);

        assertSame("Оставшийся элемент — второй ингредиент", mockFilling, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIngredientWithInvalidIndexShouldThrowException() {
        burger.removeIngredient(0);
    }

    @Test
    public void moveIngredientShouldChangeIngredientPosition() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.moveIngredient(1, 0);

        assertSame("На позиции 0 должен быть второй ингредиент", mockFilling, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveIngredientWithInvalidIndexShouldThrowException() {
        burger.moveIngredient(0, 1);
    }

    @Test
    public void getPriceShouldSumBunAndIngredientsPrices() {
        when(mockBun.getPrice()).thenReturn(50f);
        when(mockSauce.getPrice()).thenReturn(30f);
        when(mockFilling.getPrice()).thenReturn(20f);
        burger.setBuns(mockBun);
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        float actualPrice = burger.getPrice();

        assertEquals("Цена бургера должна быть 150", 150, actualPrice, 0.001);
    }

    @Test
    public void getReceiptShouldReturnFormattedStringWithNoIngredients() {
        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(100f);
        burger.setBuns(mockBun);
        String expected = String.format("(==== black bun ====)%n(==== black bun ====)%n%nPrice: %f%n", 200f);

        String receipt = burger.getReceipt();

        assertEquals("Чек без ингредиентов должен быть сформирован верно", expected, receipt);
    }

    @Test
    public void getReceiptShouldIncludeAllIngredients() {
        when(mockBun.getName()).thenReturn("white bun");
        when(mockSauce.getType()).thenReturn(IngredientType.SAUCE);
        when(mockSauce.getName()).thenReturn("hot sauce");
        when(mockFilling.getType()).thenReturn(IngredientType.FILLING);
        when(mockFilling.getName()).thenReturn("cutlet");
        burger.setBuns(mockBun);
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);
        String expected = String.format(
                "(==== white bun ====)%n= sauce hot sauce =%n= filling cutlet =%n(==== white bun ====)%n%nPrice: %f%n",
                325f
        );

        String receipt = burger.getReceipt();

        assertEquals("Чек с ингредиентами должен быть сформирован верно", expected, receipt);
    }
}
