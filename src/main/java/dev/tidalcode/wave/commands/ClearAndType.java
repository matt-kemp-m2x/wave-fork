package dev.tidalcode.wave.commands;

import com.tidal.utils.counter.TimeCounter;
import dev.tidalcode.wave.command.Command;
import dev.tidalcode.wave.command.CommandAction;
import dev.tidalcode.wave.command.CommandContext;
import dev.tidalcode.wave.command.Commands;
import dev.tidalcode.wave.exceptions.CommandExceptions;
import dev.tidalcode.wave.supplier.ObjectSupplier;
import dev.tidalcode.wave.webelement.Element;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ClearAndType extends CommandAction implements Command<Void> {

    private final Supplier<Map<Class<? extends Throwable>, Supplier<String>>> ignoredExceptions = this::ignoredEx;
    private final Element webElement = (Element) ObjectSupplier.instanceOf(Element.class);
    private final TimeCounter timeCounter = new TimeCounter();
    private CharSequence[] charSequences;

    @Override
    public void contextSetter(CommandContext context) {
        this.context = context;
        charSequences = context.getSequence();
    }

    @Override
    public CommandContext getCommandContext() {
        return context;
    }

    Function<CommandContext, Void> function = e -> {
        Function<WebElement, String> expectedValue = w -> w.getDomAttribute("value");

        WebElement element = webElement.getElement(context);
        int currentCharsCount = expectedValue.apply(element).length();

        for (int i = 0; i < currentCharsCount; i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }

        for (CharSequence c : charSequences) {
            element.sendKeys(c);
        }
        return null;
    };

    @Override
    public Function<CommandContext, Void> getFunction() {
        return function;
    }

    @Override
    public Map<Class<? extends Throwable>, Supplier<String>> ignoredEx() {
        return CommandExceptions.Of.clear();
    }

    public void clearAndTypeAction() {
        function.apply(context);
    }

    public void clearAndType() {
        timeCounter.restart();
        super.execute(Commands.ClickCommands.CLEAR_AND_TYPE.toString(), ignoredExceptions, timeCounter);
    }
}
