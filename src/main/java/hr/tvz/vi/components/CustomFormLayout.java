/*
 * CustomFormLayout CustomFormLayout.java.
 *
 */
package hr.tvz.vi.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.ValueProvider;

import hr.tvz.vi.util.Constants.StyleConstants;
import hr.tvz.vi.util.Constants.ThemeAttribute;
import hr.tvz.vi.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.html.VH4;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CustomFormLayout.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @param <T> the generic type
 * @since 8:20:09 PM Aug 11, 2021
 */
@Slf4j
public class CustomFormLayout<T> extends VVerticalLayout {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -9071058555999755515L;

  /** The form title. */
  private final VH4 formTitle = new VH4().withVisible(false);

  /** The binder. */
  @Getter
  private final Binder<T> binder;

  /** The bean. */
  private final T bean;

  /**
   * Instantiates a new custom form layout.
   */
  public CustomFormLayout(final Binder<T> topSectionBinder, T bean) {
    this.binder = topSectionBinder;
    this.bean = bean;
    Utils.removeAllThemes(this);
    getThemeList().add(ThemeAttribute.SPACING);
    setWidthFull();
    add(formTitle);
  }

  /**
   * Adds the form item.
   *
   * @param component the component
   * @param translationKey the translation key
   * @param required the required
   */
  public void addFormItem(final Component component, final String translationKey, Object... params) {
    setLabel(component, translationKey, params);
    component.getElement().setAttribute("class", StyleConstants.WIDTH_100.getName());
    add(component);
  }

  /**
   * Adds the read only item.
   *
   * @param translationKey the translation key
   * @param text the text
   */
  public Paragraph addReadOnlyItem(final String labelTranslationKey, final Component valueComponent) {
    final StringBuilder builder = new StringBuilder();
    if (StringUtils.isNotBlank(labelTranslationKey)) {
      builder.append(getTranslation(labelTranslationKey)).append(": ");
    }
    // Utils.removeAllThemes(valueComponent);
    final Paragraph paragraph = new Paragraph(new Span(builder.toString()), valueComponent);
    // paragraph.addClassName(StyleConstants.FORM_INFO.getName());
    add(paragraph);
    return paragraph;
  }

  /**
   * Adds the read only item.
   *
   * @param translationKey the translation key
   * @param text the text
   */
  public Paragraph addReadOnlyItem(final String labelTranslationKey, final String text, final String... styleClasses) {
    final StringBuilder builder = new StringBuilder();
    if (StringUtils.isNotBlank(labelTranslationKey)) {
      builder.append(getTranslation(labelTranslationKey)).append(": ");
    }
    final Span value = new Span(text);
    value.addClassNames(styleClasses);
    final Paragraph paragraph = new Paragraph(new Span(builder.toString()), value);
    // paragraph.addClassName(StyleConstants.FORM_INFO.getName());
    add(paragraph);
    return paragraph;
  }

  /**
   * Adds the save bean button.
   *
   * @return the v button
   */
  public VButton addSaveBeanButton(ComponentEventListener<ClickEvent<Button>> saveListener) {
    final VButton saveButton = new VButton(getTranslation("button.save")).withClickListener(saveListener);
    add(saveButton);
    return saveButton;
  }
  
  /**
	 * Adds the button.
	 *
	 * @param buttonKey the button key
	 * @param listener  the listener
	 * @return the v button
	 */
  public VButton addButton(String buttonKey,  ComponentEventListener<ClickEvent<Button>> listener) {
	    final VButton button = new VButton(getTranslation(buttonKey)).withClickListener(listener);
	    add(button);
	    return button;
	  }
  

  /**
   * Adds the two column items layout.
   *
   * @param left the left
   * @param right the right
   * @return the component
   */
  public Component addTwoColumnItemsLayout(Component left, Component right) {
    final VHorizontalLayout layout = new VHorizontalLayout(left).withClassName(StyleConstants.WIDTH_100.getName());
    if (null != right) {
      layout.add(right);
      right.getElement().setAttribute("class", StyleConstants.WIDTH_50.getName());
    }
    left.getElement().setAttribute("class", StyleConstants.WIDTH_50.getName());
    add(layout);
    return layout;
  }
  
  public Component addThreeColumnItemsLayout(Component left, Component middle, Component right) {
    final VHorizontalLayout layout = new VHorizontalLayout(left).withClassName(StyleConstants.WIDTH_100.getName());
    if(null != middle) {
      layout.add(middle);
      middle.getElement().setAttribute("class", StyleConstants.WIDTH_33.getName());
    }
    
    if (null != right) {
      layout.add(right);
      right.getElement().setAttribute("class", StyleConstants.WIDTH_33.getName());
    }
    left.getElement().setAttribute("class", StyleConstants.WIDTH_33.getName());
    add(layout);
    return layout;
  }


  /**
   * Adds the two column items layout.
   *
   * @param left the left
   * @param leftStyle the left style
   * @param right the right
   * @param rightStyle the right style
   * @return the component
   */
  public Component addTwoColumnItemsLayout(Component left, StyleConstants leftStyle, Component right, StyleConstants rightStyle) {
    final VHorizontalLayout layout = new VHorizontalLayout(left).withClassName(StyleConstants.WIDTH_100.getName());
    if (null != right) {
      layout.add(right);
      if (rightStyle != null) {
        right.getElement().setAttribute("class", rightStyle.getName());
      }
    }
    left.getElement().setAttribute("class", leftStyle.getName());
    add(layout);
    return layout;
  }

  /**
   * Process binder.
   *
   * @param <R> the generic type
   * @param <S> the generic type
   * @param field the field
   * @param converter the converter
   * @param validator the validator
   * @param required the required
   * @param propertyPath the property path
   */
  public <R, S> void processBinder(final HasValue<?, R> field, final Converter<R, S> converter, final Validator<? super R> validator, final boolean required,
    final String propertyPath) {
    if(bean == null || binder == null) {
      return;
    }
    final BindingBuilder<T, R> bindingBuilder = binder.forField(field);
    
    if (required) {
      bindingBuilder.asRequired(getTranslation("form.field.required"));
    }
    if (null != validator) {
      bindingBuilder.withValidator(j -> {
        if (j == null) {
          return true;
        }
        return false;
      }, "");
      bindingBuilder.withValidator(validator);
    }
    if (null != converter) {
      bindingBuilder.withConverter(converter);
    }
    bindingBuilder.bind(propertyPath);
  }
  
  public <R, S> void processBinder(final HasValue<?, R> field, final Converter<R, S> converter, final Validator<? super R> validator, final boolean required,
		    final ValueProvider<T, R> getter, final Setter<T, R> setter) {
      
        if(bean == null || binder == null) {
          return;
        }
		    final BindingBuilder<T, R> bindingBuilder = binder.forField(field);
		    
		    if (required) {
		      bindingBuilder.asRequired(getTranslation("form.field.required"));
		    }
		    if (null != validator) {
		      bindingBuilder.withValidator(j -> {
		        if (j == null) {
		          return true;
		        }
		        return false;
		      }, "");
		      bindingBuilder.withValidator(validator);
		    }
		    if (null != converter) {
		      bindingBuilder.withConverter(converter);
		    }
		    bindingBuilder.bind(getter, setter);
		  }

  /**
   * Read bead.
   */
  public void readBean() {
    if(bean == null || binder == null) {
      return;
    }
    binder.readBean(bean);
  }

  /**
   * Sets the bean.
   */
  public void setBean() {
    if(bean == null || binder == null) {
      return;
    }
    binder.setBean(bean);
  }
  
  /**
   * Validate.
   *
   * @return true, if successful
   */
  public boolean validate() {
    if(bean == null || binder == null) {
      return false;
    }
    return binder.validate().isOk();
  }
  /**
   * Sets the form title.
   *
   * @param titleKey the title key
   * @param labelParams the label params
   */
  public void setFormTitle(String titleKey, Object... labelParams) {
    formTitle.setText( getTranslation(titleKey, labelParams));
    formTitle.setVisible(true);
  }
  


  /**
   * Sets the label.
   *
   * @param component the component
   * @param taskFormModel the task form model
   */
  public void setLabel(final Component component, final String translationKey, Object... labelParams) {
    setLabel(component, false, translationKey, labelParams);
  }

  
  /**
   * Sets the label.
   *
   * @param component the component
   * @param requiredTag the required tag
   * @param translationKey the translation key
   * @param labelParams the label params
   */
  public void setLabel(final Component component, boolean requiredTag, final String translationKey, Object... labelParams) {
     String label = StringUtils.isNotEmpty(translationKey) ? getTranslation(translationKey, labelParams) : "";
    if(requiredTag) {
      label = label + "*";
    }
    component.getElement().setAttribute("label", label);
  }

  /**
   * Write bean.
   *
   * @return true, if successful
   */
  public boolean writeBean() {
    if(bean == null || binder == null) {
      return false;
    }
    try {
      binder.writeBean(bean);
      return true;
    } catch (final ValidationException e) {
      Utils.showErrorNotification(3000, Position.TOP_CENTER, getTranslation("notification.writeBean.fail"));
      return false;
    }
  }

}
