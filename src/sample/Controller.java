package sample;

import com.sun.javafx.css.Stylesheet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.controlsfx.control.RangeSlider;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ////////////////////Declarations//////////////////////
    @FXML
    private RangeSlider sliderLimits;
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button loopButton;
    @FXML
    private Slider mainSlider;
    @FXML
    private MediaView mv;
    @FXML
    private Label sliderVal;
    @FXML
    private Label upperLimitVal;
    @FXML
    private MediaView psmMv;

    private MediaPlayer mp;
    private Media me;

    @FXML
    private Label mainSliderLabel;

    Boolean loopActive = false;
    Duration stopTime;

    /////////////////////////////////////////////////////
    @Override
    public void initialize(URL location, ResourceBundle resources) {



        String path = new File("src/sample/media/Sample video.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);                                         //Setting up the video stuff

        Custom_Slider customSlider = new Custom_Slider();

        mp.setOnReady(new Runnable() {
            @Override
            public void run() {
                mainSlider = customSlider.setMainSliderValues(mainSlider, mp);          //Sets up the timescale in terms of seconds
                sliderLimits = customSlider.setLimitValues(sliderLimits, mp);

            }
        });

        mp.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                // Listens to changes of video time
                mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {       //Allows slider to move w/ video
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        mainSlider.setValue(newValue.toSeconds());



                    }

                });

            }
        });

        customSlider.sliderLimit(mainSlider, sliderLimits);                  //Limits where the main slider can go based on Range Slider

        mainSlider.valueProperty().addListener(new ChangeListener<Number>() {             //Stops slider at upper limit
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sliderVal.setText(Double.toString(mainSlider.getValue()));
                if (mainSlider.getValue() == sliderLimits.getHighValue() && loopActive == false) {
                    mp.pause();
                } else if (mainSlider.getValue() == sliderLimits.getHighValue() && loopActive == true) {
                    mp.seek(Duration.seconds(sliderLimits.getLowValue()));
                }
            }
        });
        sliderLimits.highValueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                upperLimitVal.setText(Double.toString(sliderLimits.getHighValue()));
            }
        });
        mainSlider.setOnMouseDragged(drag -> {
            mp.seek(Duration.seconds(mainSlider.getValue()));
        });
        sliderLimits.setOnMouseDragged(drag -> {
            mp.pause();
        });

        playButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements play action
            @Override
            public void handle(ActionEvent e) {
                mp.play();
                if (mainSlider.getValue() < sliderLimits.getLowValue() || mainSlider.getValue() > sliderLimits.getHighValue()) {
                    mp.seek(Duration.seconds(sliderLimits.getLowValue()));
                }
            }
        });
        stopButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements stop action
            @Override
            public void handle(ActionEvent e) {
                System.out.println(mp.getStatus());
               // mp.setOnPaused(()-> System.out.println("player paused"));
                stopTime = mp.getCurrentTime();
                mp.pause();
                System.out.println(mp.getStatus());

                mp.setOnPaused(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(mp.getStatus());
                        mp.seek(stopTime);
                    }
                });

            }
        });
        loopButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements loop action
            @Override
            public void handle(ActionEvent e) {

                mp.play();
                if(loopActive == true){
                  loopActive = false;
                }
                else {

                    loopActive = true;
                }
            }
        });
    }
}
