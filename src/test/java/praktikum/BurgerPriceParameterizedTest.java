package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Параметризованные тесты для метода getPrice() класса Burger.
 * Я проверяю расчёт цены на разных наборах булок и ингредиентов.
 * Использую реальные объекты (не моки), чтобы протестировать именно подсчет суммы.
 */
@RunWith(Parameterized.class)
public class BurgerPriceParameterizedTest {

    private final float bunPrice;
    private final float ingredient1Price;
    private final float ingredient2Price;
    private final float expectedPrice;

    public BurgerPriceParameterizedTest(float bunPrice, float ingredient1Price,
                                        float ingredient2Price, float expectedPrice) {
        this.bunPrice = bunPrice;
        this.ingredient1Price = ingredient1Price;
        this.ingredient2Price = ingredient2Price;
        this.expectedPrice = expectedPrice;
    }

    @Parameters(name = "Цена булки = {0}, ингр1 = {1}, ингр2 = {2} -> ожидаем {3}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {100, 0, 0, 200},
                {50, 30, 20, 150},
                {200, 100, 50, 550},
                {0, 10, 10, 20},
                {75, 125, 200, 475}
        });
    }

    @Test
    public void getPriceShouldReturnCorrectSum() {
        Burger burger = new Burger();
        Bun bun = new Bun("test bun", bunPrice);
        Ingredient ingredient1 = new Ingredient(IngredientType.SAUCE, "sauce", ingredient1Price);
        Ingredient ingredient2 = new Ingredient(IngredientType.FILLING, "filling", ingredient2Price);
        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float actualPrice = burger.getPrice();

        assertEquals("Цена бургера рассчитана неверно", expectedPrice, actualPrice, 0.001);
    }
}
