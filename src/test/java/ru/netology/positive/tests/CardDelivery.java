package ru.netology.positive.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

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
//        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "874x769";
        open("http://localhost:9999");
    }

    @Test
    void shouldCorrectlyFilledOutForm() throws InterruptedException {
        $("[data-test-id=city] .input__control").val("Симферополь");
        String date = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        Thread.sleep(2000);
        $x("//input[@type= 'tel']").val(date);
        $("[data-test-id=name] .input__control").val("Шувалов Дмитрий");
        $("[data-test-id=\"phone\"] input").val("+79788885522");
        $("[data-test-id='agreement']").click();
        $$("[role=\"button\"]").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " +date))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldEnterHyphenatedName(){
        $("[data-test-id=city] .input__control").val("Казань");
        String date = LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
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
}
