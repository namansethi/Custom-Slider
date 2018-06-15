package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.controlsfx.control.RangeSlider;
import javafx.scene.image.ImageView;

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
    private ToggleButton loopButton;
    @FXML
    private Slider mainSlider;
    @FXML
    private MediaView mv;
    @FXML
    private MediaView psmMv;
    @FXML
    private Label sliderVal;
    @FXML
    private Label upperLimitVal;
    @FXML
    private ImageView noVideoMv;
    @FXML
    private ImageView noVideoPsm;
    @FXML
    private Label mainSliderLabel;

    private MediaPlayer mp;
    private Media me;
    private MediaPlayer psmMp;
    private Media psmMe;
    private Boolean loopActive = false;
    private Duration stopTime;
    private MediaPlayer biggerDuration;
    private MediaPlayer smallerDuration;


    /////////////////////////////////////////////////////
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        String path = new File("src/sample/media/Sample video.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);                                         //Setting up the video  1

        String path2 = new File("src/sample/media/Sample video 2.mp4").getAbsolutePath();
        psmMe = new Media(new File(path2).toURI().toString());
        psmMp = new MediaPlayer(psmMe);
        psmMv.setMediaPlayer(psmMp);                                         //Setting up the video  2

        File file = new File("src/sample/media/no video.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView noVideoMv = new ImageView(image);
        ImageView noVideoPsm = new ImageView(image);

        Custom_Slider customSlider = new Custom_Slider();
        biggerDuration = mp;
        smallerDuration = mp;




        mp.setOnReady(new Runnable() {
            @Override
            public void run() {

                biggerDuration = customSlider.setBiggerDuration(mp, psmMp);
                smallerDuration = customSlider.setSmallerDuration(mp, psmMp);

                mainSlider = customSlider.setMainSliderValues(mainSlider, biggerDuration);          //Sets up the timescale in terms of seconds
                sliderLimits = customSlider.setLimitValues(sliderLimits, biggerDuration);           // Uses the biggest video for timescale

            }


        });


        biggerDuration.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                // Listens to changes of video time
                biggerDuration.currentTimeProperty().addListener(new ChangeListener<Duration>() {       //Allows slider to move w/ video
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
                    psmMp.pause();
                } else if (mainSlider.getValue() == sliderLimits.getHighValue() && loopActive == true) {
                    mp.seek(Duration.seconds(sliderLimits.getLowValue()));
                    psmMp.seek(Duration.seconds(sliderLimits.getLowValue()));
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
            psmMp.seek(Duration.seconds(mainSlider.getValue()));
        });
        mainSlider.setOnMouseClicked(click -> {
            mp.seek(Duration.seconds(mainSlider.getValue()));
            psmMp.seek(Duration.seconds(mainSlider.getValue()));
        });
        sliderLimits.setOnMouseDragged(drag -> {
            mp.pause();
            psmMp.pause();
        });

        playButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements play action
            @Override
            public void handle(ActionEvent e) {
                mp.play();
                psmMp.play();
                if (mainSlider.getValue() < sliderLimits.getLowValue() || mainSlider.getValue() > sliderLimits.getHighValue()) {
                    mp.seek(Duration.seconds(sliderLimits.getLowValue()));
                    psmMp.seek(Duration.seconds(sliderLimits.getLowValue()));
                }
            }
        });
        stopButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements stop action
            @Override
            public void handle(ActionEvent e) {
                // mp.setOnPaused(()-> System.out.println("player paused"));
                stopTime = biggerDuration.getCurrentTime();
                mp.pause();
                psmMp.pause();

                mp.setOnPaused(new Runnable() {
                    @Override
                    public void run() {

                        if (mainSlider.getValue() != sliderLimits.getHighValue()) {
                            psmMp.seek(stopTime);
                            mp.seek(stopTime);
                        }
                    }
                });

            }
        });
        loopButton.setOnAction(new EventHandler<ActionEvent>() {                        //Implements loop action
            @Override
            public void handle(ActionEvent e) {

                if (loopActive == true) {
                    loopActive = false;
                } else {

                    loopActive = true;
                }
            }
        });
    }
}
