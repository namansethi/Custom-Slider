package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import org.controlsfx.control.RangeSlider;
import org.w3c.dom.ranges.Range;

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
                //  SliderPos2.setText(String.format("%.2f", new_val));

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
    public RangeSlider setLimitValues(RangeSlider sliderLimits, MediaPlayer mp) {
        sliderLimits.setMin(mp.getStartTime().toSeconds());
        sliderLimits.setMax(mp.getStopTime().toSeconds());
        sliderLimits.setLowValue(mp.getStartTime().toSeconds());
        sliderLimits.setHighValue(mp.getTotalDuration().toSeconds());

        return sliderLimits;
    }
    //Sets up tick units, min and max values and the position of main Slider.
    public Slider setMainSliderValues(Slider mainSlider, MediaPlayer mp) {
        mainSlider.setMin(mp.getStartTime().toSeconds());
        mainSlider.setMax(mp.getStopTime().toSeconds());
        mainSlider.setMajorTickUnit(1);
        return mainSlider;
    }
}
