package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import org.controlsfx.control.RangeSlider;

public class Custom_Slider {


    Slider mainSlider;
    RangeSlider sliderLimits;

    public Custom_Slider() {
    }

    ;

    public Slider getMainSlider() {
        return mainSlider;
    }

    public void setMainSlider(Slider mainSlider) {
        this.mainSlider = mainSlider;
    }

    public RangeSlider getSliderLimits() {
        return sliderLimits;
    }

    public void setSliderLimits(RangeSlider sliderLimits) {
        this.sliderLimits = sliderLimits;
    }

    public void sliderLimit(Slider mainSlider, RangeSlider sliderLimits) {    //This method sets the limits for the main Slider based of the upper and lower bounds.

        mainSlider.valueProperty().addListener(new ChangeListener<Number>() {                  //Listener on main slider
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {

                if (new_val.doubleValue() < sliderLimits.getLowValue()) {
                    mainSlider.adjustValue(sliderLimits.getLowValue());

                } else if (new_val.doubleValue() > sliderLimits.getHighValue()) {
                    mainSlider.adjustValue(sliderLimits.getHighValue());

                }
            }
        });
        //Returns slider 2 as that is the main slider
    }
    //Sets up tick units, min and max values and the position of upper bound.
    public RangeSlider setLimitValues(RangeSlider sliderLimits, MediaPlayer biggerDuration) {
        sliderLimits.setMin(biggerDuration.getStartTime().toSeconds());
        sliderLimits.setMax(biggerDuration.getStopTime().toSeconds());
        sliderLimits.setLowValue(biggerDuration.getStartTime().toSeconds());
        sliderLimits.setHighValue(biggerDuration.getTotalDuration().toSeconds());

        return sliderLimits;
    }
    //Sets up tick units, min and max values and the position of main Slider.
    public Slider setMainSliderValues(Slider mainSlider, MediaPlayer biggerDuration) {
        mainSlider.setMin(biggerDuration.getStartTime().toSeconds());
        mainSlider.setMax(biggerDuration.getStopTime().toSeconds());
        mainSlider.setMajorTickUnit(1);
        return mainSlider;
    }
    public MediaPlayer setBiggerDuration(MediaPlayer mp, MediaPlayer psmMp) {
        System.out.println(psmMp.getTotalDuration());
        if (mp.getTotalDuration().greaterThan(psmMp.getTotalDuration())){
        return mp;

        }
        return psmMp;
    }
    public MediaPlayer setSmallerDuration(MediaPlayer mp, MediaPlayer psmMp) {

        if (mp.getTotalDuration().lessThan(psmMp.getTotalDuration())){
            return mp;

        }
        return psmMp;
    }
}
