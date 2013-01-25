package org.vaadin.alump.distributionbar;

import java.util.Random;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("demo")
public class DistributionBarDemoUI extends UI {

    private DistributionBar barOne;
    private DistributionBar barTwo;
    private DistributionBar barThree;
    private DistributionBar barFour;
    private DistributionBar barFive;
    private DistributionBar barSix;

    final static private int BAR_ONE_PARTS = 2;
    final static private int BAR_TWO_PARTS = 3;
    final static private int BAR_THREE_PARTS = 6;
    final static private int BAR_FOUR_PARTS = 10;
    final static private int BAR_FIVE_PARTS = 2;
    final static private int BAR_SIX_PARTS = 11;

    @Override
    protected void init(VaadinRequest request) {
        this.setContent(buildView());
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
        barOne.setCaption("Senate:");
        barOne.setWidth("100%");
        barOne.addStyleName("my-bar-one");
        barOne.setPartTooltip(0, "Republican Party");
        barOne.setPartTooltip(1, "Democratic Party");
        layout.addComponent(barOne);
        layout.setComponentAlignment(barOne, Alignment.MIDDLE_CENTER);

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
        layout.setComponentAlignment(barTwo, Alignment.MIDDLE_CENTER);

        barThree = new DistributionBar(BAR_THREE_PARTS);
        barThree.setCaption("Maaaany parts with default styling");
        barThree.setWidth("100%");
        barThree.addStyleName("my-bar-three");
        layout.addComponent(barThree);
        layout.setComponentAlignment(barThree, Alignment.MIDDLE_CENTER);

        barFour = new DistributionBar(BAR_FOUR_PARTS);
        barFour.setCaption("CSS tricks");
        barFour.setWidth("100%");
        barFour.addStyleName("my-bar-four");
        barFour.setPartTooltip(BAR_FOUR_PARTS - 1, "Wow! You found this.");
        layout.addComponent(barFour);
        layout.setComponentAlignment(barFour, Alignment.MIDDLE_CENTER);

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
        layout.setComponentAlignment(barFive, Alignment.MIDDLE_CENTER);

        barSix = new DistributionBar(BAR_SIX_PARTS);
        barSix.setCaption("Change in part count:");
        barSix.setWidth("100%");
        barSix.addStyleName("my-bar-six");
        layout.addComponent(barSix);
        layout.setComponentAlignment(barSix, Alignment.MIDDLE_CENTER);

        return layout;

    }

    private final Button.ClickListener randomButtonListener = new Button.ClickListener() {

        private static final long serialVersionUID = -4454445076337898590L;

        public void buttonClick(ClickEvent event) {

            Random random = new Random();

            int chairs = 100;
            int groupA = random.nextInt(chairs);

            barOne.setPartSize(0, groupA);
            barOne.setPartTooltip(0, "<b>Republican Party: " + groupA
                    + " seats</b><br/>" + "Lorem ipsum...<br/>...ipsum Lorem.");
            barOne.setPartSize(1, chairs - groupA);
            barOne.setPartTooltip(1, "<b>Democratic Party: "
                    + (chairs - groupA)
                    + " seats</b><br/>Lorem ipsum...<br/>...ipsum Lorem.");

            for (int i = 0; i < BAR_TWO_PARTS; ++i) {
                barTwo.setPartSize(i, random.nextInt(20));
            }

            for (int i = 0; i < BAR_THREE_PARTS; ++i) {
                int value = random.nextInt(50);
                barThree.setPartSize(i, value);
                barThree.setPartTooltip(i, "part" + i + ", with size: " + value);
            }

            for (int i = 0; i < BAR_FOUR_PARTS; ++i) {
                barFour.setPartSize(i, random.nextInt(10));
            }

            for (int i = 0; i < BAR_FIVE_PARTS; ++i) {
                barFive.setPartSize(i, random.nextInt(10000000));
            }

            int newSize = 2 + random.nextInt(9);
            barSix.setNumberOfParts(newSize);
            for (int i = 0; i < newSize; ++i) {
                barSix.setPartSize(i, random.nextInt(5));
            }

        }
    };

}
