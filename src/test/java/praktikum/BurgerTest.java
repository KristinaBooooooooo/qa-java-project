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
    private Ingredient mockIngredient1;

    @Mock
    private Ingredient mockIngredient2;

    @Before
    public void setUp() {
        burger = new Burger();
        when(mockBun.getName()).thenReturn("Mock Bun");
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockIngredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient1.getName()).thenReturn("Mock Sauce");
        when(mockIngredient1.getPrice()).thenReturn(50f);
        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("Mock Filling");
        when(mockIngredient2.getPrice()).thenReturn(75f);
    }

    @Test
    public void setBunsShouldAssignBun() {
        burger.setBuns(mockBun);

        assertSame("Булка должна быть сохранена", mockBun, burger.bun);
    }

    @Test
    public void addIngredientShouldAddIngredientToList() {
        burger.addIngredient(mockIngredient1);

        assertEquals("Размер списка должен быть 1", 1, burger.ingredients.size());
        assertSame("Добавленный ингредиент должен быть в списке", mockIngredient1, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientShouldDeleteIngredientByIndex() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        burger.removeIngredient(0);

        assertEquals("После удаления размер должен стать 1", 1, burger.ingredients.size());
        assertSame("Оставшийся элемент — второй ингредиент", mockIngredient2, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIngredientWithInvalidIndexShouldThrowException() {
        burger.removeIngredient(0);
    }

    @Test
    public void moveIngredientShouldChangeIngredientPosition() {
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        burger.moveIngredient(1, 0);

        assertSame("На позиции 0 должен быть второй ингредиент", mockIngredient2, burger.ingredients.get(0));
        assertSame("На позиции 1 должен быть первый ингредиент", mockIngredient1, burger.ingredients.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveIngredientWithInvalidIndexShouldThrowException() {
        burger.moveIngredient(0, 1);
    }

    @Test
    public void getPriceShouldSumBunAndIngredientsPrices() {
        when(mockBun.getPrice()).thenReturn(50f);
        when(mockIngredient1.getPrice()).thenReturn(30f);
        when(mockIngredient2.getPrice()).thenReturn(20f);
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        float actualPrice = burger.getPrice();

        assertEquals("Цена бургера должна быть 150", 150, actualPrice, 0.001);
        verify(mockBun, times(1)).getPrice();
        verify(mockIngredient1, times(1)).getPrice();
        verify(mockIngredient2, times(1)).getPrice();
    }

    @Test
    public void getReceiptShouldReturnFormattedStringWithNoIngredients() {
        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(100f);
        burger.setBuns(mockBun);

        String receipt = burger.getReceipt();

        String expectedEnd = String.format("(==== black bun ====)%n%nPrice: %f%n", burger.getPrice());
        assertTrue("Чек должен начинаться с верхней булки", receipt.startsWith("(==== black bun ====)"));
        assertTrue("Чек должен заканчиваться нижней булкой и ценой", receipt.endsWith(expectedEnd));
    }

    @Test
    public void getReceiptShouldIncludeAllIngredients() {
        when(mockBun.getName()).thenReturn("white bun");
        when(mockIngredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(mockIngredient1.getName()).thenReturn("hot sauce");
        when(mockIngredient2.getType()).thenReturn(IngredientType.FILLING);
        when(mockIngredient2.getName()).thenReturn("cutlet");
        burger.setBuns(mockBun);
        burger.addIngredient(mockIngredient1);
        burger.addIngredient(mockIngredient2);

        String receipt = burger.getReceipt();

        assertTrue("Чек должен содержать верхнюю булку", receipt.contains("(==== white bun ====)"));
        assertTrue("Чек должен содержать соус", receipt.contains("= sauce hot sauce ="));
        assertTrue("Чек должен содержать начинку", receipt.contains("= filling cutlet ="));
        assertTrue("Чек должен содержать цену", receipt.contains("Price:"));
    }
}
