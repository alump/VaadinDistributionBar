package org.vaadin.alump.distributionbar;

import java.util.Random;

import com.vaadin.annotations.VaadinServletConfiguration;
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

    private DistributionBar barOne;
    private DistributionBar barTwo;
    private DistributionBar barThree;
    private DistributionBar barFour;
    private DistributionBar barFive;
    private DistributionBar barSix;
    private DistributionBar barSeven;

    final static private int BAR_ONE_PARTS = 2;
    final static private int BAR_TWO_PARTS = 3;
    final static private int BAR_THREE_PARTS = 6;
    final static private int BAR_FOUR_PARTS = 10;
    final static private int BAR_FIVE_PARTS = 2;
    final static private int BAR_SIX_PARTS = 11;
    final static private int BAR_SEVEN_PARTS = 3;

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

        Button randomButton = new Button(
                "Click here to update the bars values and tooltips");
        randomButton.addClickListener(randomButtonListener);
        layout.addComponent(randomButton);

        barOne = new DistributionBar(BAR_ONE_PARTS);
        barOne.setCaption("Senate (with clicks):");
        barOne.setWidth("100%");
        barOne.addStyleName("my-bar-one");
        barOne.setPartTooltip(0, "Republican Party");
        barOne.setPartTooltip(1, "Democratic Party");
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

        barTwo = new DistributionBar(BAR_TWO_PARTS);
        barTwo.setCaption("Do people like nicer backgrounds?");
        barTwo.setWidth("100%");
        barTwo.addStyleName("my-bar-two");
        // REMEMBER THAT tooltip is XHTML! Escape < and >!
        barTwo.setPartTooltip(0, "Check the one on the right-&gt;");
        barTwo.setPartTooltip(1,
                "<img src=\"http://alump.iki.fi/avatar.png\" />");
        barTwo.setPartTooltip(2, "&lt;- Check the one on the left");
        layout.addComponent(barTwo);

        barThree = new DistributionBar(BAR_THREE_PARTS);
        barThree.setCaption("Maaaany parts with default styling");
        barThree.setWidth("100%");
        barThree.addStyleName("my-bar-three");
        layout.addComponent(barThree);

        barFour = new DistributionBar(BAR_FOUR_PARTS);
        barFour.setCaption("CSS tricks");
        barFour.setWidth("100%");
        barFour.addStyleName("my-bar-four");
        barFour.setPartTooltip(BAR_FOUR_PARTS - 1, "Wow! You found this.");
        barFour.setPartStyleName(BAR_FOUR_PARTS - 1, "hidden-part");
        layout.addComponent(barFour);

        barFive = new DistributionBar(BAR_FIVE_PARTS);
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

        barSix = new DistributionBar(BAR_SIX_PARTS);
        barSix.setCaption("Change in part count:");
        barSix.setWidth("100%");
        barSix.addStyleName("my-bar-six");
        layout.addComponent(barSix);

        HorizontalLayout resizingLayout = new HorizontalLayout();
        resizingLayout.setSpacing(true);
        resizingLayout.setWidth("100%");
        layout.addComponent(resizingLayout);
        resizingLayout.setCaption("Dynamic layout test:");

        changingLabel = new Label("changing label");
        changingLabel.setSizeUndefined();
        resizingLayout.addComponent(changingLabel);

        barSeven = new DistributionBar(BAR_SEVEN_PARTS);
        barSeven.addStyleName("my-bar-seven");
        barSeven.setWidth("100%");
        resizingLayout.addComponent(barSeven);
        resizingLayout.setExpandRatio(barSeven, 1.0f);
        for(int i = 0; i < BAR_SEVEN_PARTS; ++i) {
            barSeven.setPartTitle(i, "foo");
            barSeven.setPartSize(i, 1 + i);
        }

        return layout;

    }

    private final Button.ClickListener randomButtonListener = new Button.ClickListener() {

        public void buttonClick(ClickEvent event) {

            int chairs = 100;
            int groupA = rand.nextInt(chairs);

            barOne.setPartSize(0, groupA);
            barOne.setPartTooltip(0, "<b>Republican Party: " + groupA
                    + " seats</b><br/>" + "Lorem ipsum...<br/>...ipsum Lorem.");
            barOne.setPartSize(1, chairs - groupA);
            barOne.setPartTooltip(1, "<b>Democratic Party: "
                    + (chairs - groupA)
                    + " seats</b><br/>Lorem ipsum...<br/>...ipsum Lorem.");

            for (int i = 0; i < BAR_TWO_PARTS; ++i) {
                barTwo.setPartSize(i, rand.nextInt(20));
            }

            for (int i = 0; i < BAR_THREE_PARTS; ++i) {
                int value = rand.nextInt(50);
                barThree.setPartSize(i, value);
                barThree.setPartTooltip(i, "part" + i + ", with size: " + value);
            }

            for (int i = 0; i < BAR_FOUR_PARTS; ++i) {
                barFour.setPartSize(i, rand.nextInt(10));
            }

            for (int i = 0; i < BAR_FIVE_PARTS; ++i) {
                barFive.setPartSize(i, rand.nextInt(10000000));
            }

            int newSize = 2 + rand.nextInt(9);
            barSix.setNumberOfParts(newSize);
            for (int i = 0; i < newSize; ++i) {
                barSix.setPartSize(i, rand.nextInt(5));
            }

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

        }
    };

}
