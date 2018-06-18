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
    }
    public void checkWitinBound(Slider mainSlider, RangeSlider sliderLimits){


    }
    //Sets up tick units, min and max values and the position of upper bound.
    public RangeSlider setLimitValues(RangeSlider sliderLimits, MediaPlayer smallerDuration) {
        sliderLimits.setMin(smallerDuration.getStartTime().toSeconds());
        sliderLimits.setMax(smallerDuration.getStopTime().toSeconds());
        sliderLimits.setLowValue(smallerDuration.getStartTime().toSeconds());
        sliderLimits.setHighValue(smallerDuration.getTotalDuration().toSeconds());

        return sliderLimits;
    }
    //Sets up tick units, min and max values and the position of main Slider.
    public Slider setMainSliderValues(Slider mainSlider, MediaPlayer smallerDuration) {
        mainSlider.setMin(smallerDuration.getStartTime().toSeconds());
        mainSlider.setMax(smallerDuration.getStopTime().toSeconds());
        mainSlider.setMajorTickUnit(1);
        return mainSlider;
    }

    public MediaPlayer setSmallerDuration(MediaPlayer mp, MediaPlayer psmMp) {

        if (mp.getTotalDuration().lessThan(psmMp.getTotalDuration())){
            return mp;

        }
        return psmMp;
    }
}
