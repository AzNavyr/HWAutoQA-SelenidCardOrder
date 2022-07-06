import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDelivery {

    @BeforeEach
    void setAll(){
        Configuration.browser = "firefox";
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "874x769";
        open("http://localhost:9999");
    }

    //===================ToDO Positive test scenario: ==================

    @Test
    void shouldCorrectlyFilledOutForm(){
        $("[data-test-id=city] .input__control").val("Симферополь");
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " +date))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldEnterHyphenatedName(){
        $("[data-test-id=city] .input__control").val("Симферополь");
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Максим-Иван");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " +date))
                .shouldBe(visible, Duration.ofSeconds(15));
    }


    //=================ToDo Negative test scenario: ====================

    @Test
    void shouldTestingEmptyCity(){
        $("[data-test-id=city] .input__control").val("");
//        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
//        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
//        $("[data-test-id=\"phone\"] input").val("+79788885522");
//        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldWrongCity(){
        $("[data-test-id=city] .input__control").val("Simferopol");
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(visible, Duration.ofSeconds(10));

//        assertEquals("Доставка в выбранный город недоступна",$("[class=\"input__sub\"]").shouldBe(visible, Duration.ofSeconds(10)).getText() );
    }

    @Test
    void shouldEnterTodayDate(){
        $("[data-test-id=city] .input__control").val("Симферополь");
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldEnterWrongName(){
        $("[data-test-id=city] .input__control").val("Симферополь");
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Ivanov Petr");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void shouldEnterTwelveDigitsOfTheNumber(){
        $("[data-test-id=city] .input__control").val("Москва");
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
        $("[data-test-id=\"phone\"] input").val("+711223344556");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(Condition.exactText("Забронировать")).click();
//        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible, Duration.ofSeconds(10));
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",$("[data-test-id = 'phone'] .input__sub").shouldBe(visible, Duration.ofSeconds(10)).getText() );

    }
}
