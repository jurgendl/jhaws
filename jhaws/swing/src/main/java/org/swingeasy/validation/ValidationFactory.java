package org.swingeasy.validation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.swingeasy.HasValue;
import org.swingeasy.Messages;
import org.swingeasy.ValueChangeListener;

/**
 * @author Jurgen
 */
public class ValidationFactory {
    protected Translator translator = null;

    public Translator getTranslator() {
        if (this.translator == null) {
            this.translator = new Translator() {

                @Override
                public String getString(String key, Object... arguments) {
                    return Messages.getInstance().getString(key, arguments);
                }
            };
        }
        return this.translator;
    }

    public <T> EValidationMessageI install(final EValidationPane parentPane, final HasValue<T> component, final List<Validator<T>> validators) {
        final EValidationMessageI message = new EValidationMessage(parentPane, JComponent.class.cast(component)).stsi();

        ValueChangeListener<T> listener = new ValueChangeListener<T>() {
            @Override
            public void valueChanged(T value) {
                StringBuilder sb = new StringBuilder();
                boolean valid = true;
                for (Validator<T> validator : validators) {
                    if (!validator.isValid(null, value)) {
                        if (!valid) {
                            sb.insert(0, "\u2022 ");
                            sb.append("<br/>\u2022 ");
                        }
                        valid = false;
                        sb.append(ValidationFactory.this.getTranslator().getString(validator.getMessageKey(), validator.getArguments(value)));
                    }
                }
                if (!valid) {
                    message.setIsInvalid("<html>" + sb.toString() + "</html>");
                } else {
                    message.setIsValid();
                }
            }
        };

        component.addValueChangeListener(listener);

        return message;
    }

    public <T> EValidationMessageI install(final EValidationPane parentPane, final HasValue<T> component, final Validator<T> validator1) {
        List<Validator<T>> validators = new ArrayList<Validator<T>>();
        validators.add(validator1);
        return this.install(parentPane, component, validators);
    }

    public <T> EValidationMessageI install(final EValidationPane parentPane, final HasValue<T> component, final Validator<T> validator1,
            final Validator<T> validator2) {
        List<Validator<T>> validators = new ArrayList<Validator<T>>();
        validators.add(validator1);
        validators.add(validator2);
        return this.install(parentPane, component, validators);
    }

    public <T> EValidationMessageI install(final EValidationPane parentPane, final HasValue<T> component, final Validator<T> validator1,
            final Validator<T> validator2, final Validator<T> validator3) {
        List<Validator<T>> validators = new ArrayList<Validator<T>>();
        validators.add(validator1);
        validators.add(validator2);
        validators.add(validator3);
        return this.install(parentPane, component, validators);
    }

    public <T> EValidationMessageI install(final EValidationPane parentPane, final HasValue<T> component, final Validator<T> validator1,
            final Validator<T> validator2, final Validator<T> validator3, final Validator<T> validator4) {
        List<Validator<T>> validators = new ArrayList<Validator<T>>();
        validators.add(validator1);
        validators.add(validator2);
        validators.add(validator3);
        validators.add(validator4);
        return this.install(parentPane, component, validators);
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }
}
