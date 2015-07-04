package org.vaadin.alump.distributionbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.vaadin.alump.distributionbar.DistributionBar.DistributionBarClickListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button.ClickEvent;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@Theme("demo")
@Title("Distribution Bar Demo")

public class DistributionBarDemoUI extends UI {

    @WebServlet(value = "/*")
    @VaadinServletConfiguration(productionMode = false, ui = DistributionBarDemoUI.class, widgetset = "org.vaadin.alump.distributionbar.demo.widgetset.DBarDemoWidgetset")
    public static class FancyLayoutsUIServlet extends VaadinServlet {
    }

    private List<DistributionBar> bars = new ArrayList<DistributionBar>();

    final static private int BAR_ONE_PARTS = 2;
    final static private int BAR_TWO_PARTS = 3;
    final static private int BAR_THREE_PARTS = 6;
    final static private int BAR_FOUR_PARTS = 10;
    final static private int BAR_FIVE_PARTS = 2;
    final static private int BAR_SIX_PARTS = 11;
    final static private int BAR_SEVEN_PARTS = 3;
    final static private int BAR_EIGHT_PARTS = 8;

    private final Random rand = new Random(0xDEADBEEF);

    private Label changingLabel;

    @Override
    protected void init(VaadinRequest request) {
        setContent(buildView());
    }

    private ComponentContainer buildView() {

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setSpacing(true);
        layout.setMargin(true);

        Label header = new Label(
                "Distribution Bar Demo! Remember to push the button! Tooltips are shown when you move mouse above the distribution bars. Not all parts have tooltips in this demo.");
        layout.addComponent(header);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth("100%");
        buttonLayout.setSpacing(true);
        layout.addComponent(buttonLayout);

        Button randomButton = new Button(
                "Randomize");
        randomButton.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                randomUpdate(false);

            }
        });
        buttonLayout.addComponent(randomButton);

        Button random2Button = new Button(
                "Randomize2");
        random2Button.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                randomUpdate(true);

            }
        });
        buttonLayout.addComponent(random2Button);

        Button themeButton = new Button("Toggle theme");
        themeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                if (getUI().getTheme().equals("demo2")) {
                    getUI().setTheme("demo");
                } else {
                    getUI().setTheme("demo2");
                }
            }
        });
        buttonLayout.addComponent(themeButton);

        CheckBox shrink = new CheckBox("Shrink zero values");
        shrink.setImmediate(true);
        shrink.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean shrink = (Boolean) event.getProperty().getValue();
                for (DistributionBar bar : bars) {
                    bar.setZeroSizedVisible(!shrink);
                }
            }
        });
        buttonLayout.addComponent(shrink);


        DistributionBar barOne = new DistributionBar(BAR_ONE_PARTS);
        barOne.setCaption("Senate (with clicks):");
        barOne.setWidth("100%");
        barOne.addStyleName("my-bar-one");
        barOne.addDistributionBarClickListener(new DistributionBarClickListener() {

            @Override
            public void onItemClicked(int index) {
                if (index == 0) {
                    Notification.show("Republican clicked!");
                } else {
                    Notification.show("Democratic clicked!");
                }
            }
        });
        layout.addComponent(barOne);
        bars.add(barOne);

        DistributionBar barTwo = new DistributionBar(BAR_TWO_PARTS);
        barTwo.setCaption("Do people like nicer backgrounds?");
        barTwo.setWidth("100%");
        barTwo.addStyleName("my-bar-two");
        // REMEMBER THAT tooltip is XHTML! Escape < and >!
        barTwo.setPartTooltip(0, "Check the one on the right-&gt;");
        barTwo.setPartTooltip(1,
                "<img src=\"http://alump.iki.fi/avatar.png\" />");
        barTwo.setPartTooltip(2, "&lt;- Check the one on the left");
        layout.addComponent(barTwo);
        bars.add(barTwo);

        DistributionBar barThree = new DistributionBar(BAR_THREE_PARTS);
        barThree.setCaption("Maaaany parts with default styling");
        barThree.setWidth("100%");
        barThree.addStyleName("my-bar-three");
        layout.addComponent(barThree);
        bars.add(barThree);

        DistributionBar barFour = new DistributionBar(BAR_FOUR_PARTS);
        barFour.setCaption("CSS tricks");
        barFour.setWidth("100%");
        barFour.addStyleName("my-bar-four");
        barFour.setPartTooltip(BAR_FOUR_PARTS - 1, "Wow! You found this.");
        barFour.setPartStyleName(BAR_FOUR_PARTS - 1, "hidden-part");
        layout.addComponent(barFour);
        bars.add(barFour);

        DistributionBar barFive = new DistributionBar(BAR_FIVE_PARTS);
        barFive.setCaption("Vote results:");
        barFive.setWidth("100%");
        barFive.addStyleName("my-bar-five");
        barFive.setPartTooltip(
                0,
                "<span style=\"color: green; font-size: 200%; font-weight: bold;\">YES! I LIKE IT!</span>");
        barFive.setPartTooltip(
                1,
                "<span style=\"color: red; font-size: 200%; font-weight: bold;\">NO WAY!</span>");
        barFive.setPartTitle(1, "NO!");
        layout.addComponent(barFive);
        bars.add(barFive);

        DistributionBar barSix = new DistributionBar(BAR_SIX_PARTS);
        barSix.setCaption("Change in part count:");
        barSix.setWidth("100%");
        barSix.addStyleName("my-bar-six");
        layout.addComponent(barSix);
        bars.add(barSix);

        HorizontalLayout resizingLayout = new HorizontalLayout();
        resizingLayout.setSpacing(true);
        resizingLayout.setWidth("100%");
        layout.addComponent(resizingLayout);
        resizingLayout.setCaption("Dynamic layout test:");

        changingLabel = new Label("changing label");
        changingLabel.setSizeUndefined();
        resizingLayout.addComponent(changingLabel);

        DistributionBar barSeven = new DistributionBar(BAR_SEVEN_PARTS);
        barSeven.addStyleName("my-bar-seven");
        barSeven.setWidth("100%");
        resizingLayout.addComponent(barSeven);
        resizingLayout.setExpandRatio(barSeven, 1.0f);
        for(int i = 0; i < BAR_SEVEN_PARTS; ++i) {
            barSeven.setPartTitle(i, "foo");
            barSeven.setPartSize(i, 1 + i);
        }
        bars.add(barSeven);

        DistributionBar barEight = new DistributionBar(BAR_EIGHT_PARTS);
        barEight.setCaption("Override of min width of 100 pixels");
        barEight.addStyleName("my-bar-eight");
        barEight.setWidth(300, Unit.PIXELS);
        barEight.setMinPartWidth(100);
        layout.addComponent(barEight);
        bars.add(barEight);

        return layout;

    }

    private final Button.ClickListener randomButtonListener = new Button.ClickListener() {

        public void buttonClick(ClickEvent event) {
            randomUpdate(false);

        }
    };

    private void randomUpdate(boolean useZeros) {

        DistributionBar barOne = bars.get(0);
        int chairs = 100;
        int groupA = useZeros ? (rand.nextInt(2) * 25) : rand.nextInt(chairs + 1);
        int groupB = chairs - groupA;
        barOne.setPartSize(0, groupA);
        barOne.setPartSize(1, groupB);

        // ----

        DistributionBar barTwo = bars.get(1);
        for (int i = 0; i < BAR_TWO_PARTS; ++i) {
            if(useZeros && i > 0 && i < (BAR_TWO_PARTS - 1) && rand.nextBoolean()) {
                barTwo.setPartSize(i, 0);
            } else {
                barTwo.setPartSize(i, rand.nextInt(20));
            }
        }

        // ----

        DistributionBar barThree = bars.get(2);
        for (int i = 0; i < BAR_THREE_PARTS; ++i) {
            int value;
            if(useZeros && rand.nextBoolean()) {
                value = 0;
            } else {
                value = rand.nextInt(50);
            }
            barThree.setPartSize(i, value);
            barThree.setPartTooltip(i, "part" + i + ", with size: " + value);
        }

        // ----

        DistributionBar barFour = bars.get(3);
        for (int i = 0; i < BAR_FOUR_PARTS; ++i) {
            if(useZeros && rand.nextBoolean()) {
                barFour.setPartSize(i, 0);
            } else {
                barFour.setPartSize(i, rand.nextInt(10));
            }
        }

        // ----

        DistributionBar barFive = bars.get(4);
        for (int i = 0; i < BAR_FIVE_PARTS; ++i) {
            if(useZeros && rand.nextBoolean()) {
                barFive.setPartSize(i, rand.nextInt(1));
            } else {
                barFive.setPartSize(i, rand.nextInt(10000000));
            }
        }

        // ----

        DistributionBar barSix = bars.get(5);
        int newSize = 1 + rand.nextInt(9);
        barSix.setNumberOfParts(newSize);
        for (int i = 0; i < newSize; ++i) {
            barSix.setPartSize(i, rand.nextInt(5));
        }

        // ----

        DistributionBar barSeven = bars.get(6);
        StringBuilder sb = new StringBuilder();
        sb.append("changing label ");
        int letters = rand.nextInt(5);
        for(int i = 0; i < letters; ++i) {
            sb.append("x");
        }
        changingLabel.setValue(sb.toString());
        for (int i = 0; i < BAR_SEVEN_PARTS; ++i) {
            barSeven.setPartSize(i, 5 + rand.nextInt(15));
        }

        // ----

        DistributionBar barEight = bars.get(7);
        for(int i = 0; i < BAR_EIGHT_PARTS; ++i) {
            barEight.setPartSize(i, 1 + rand.nextInt(15));
        }

    }

}
