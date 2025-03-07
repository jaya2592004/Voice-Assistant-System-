import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.RecognizerState;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.frontend.util.Microphone;

import java.io.File;

public class VoiceAssistant {
    public static void main(String[] args) {
        try {
            ConfigurationManager cm = new ConfigurationManager(new File("config.xml").toURI().toURL());
            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
            recognizer.allocate();

            Microphone microphone = (Microphone) cm.lookup("microphone");
            if (!microphone.startRecording()) {
                System.out.println("Microphone is not working.");
                return;
            }

            System.out.println("Say a command: (e.g., 'hello')");
            while (true) {
                var result = recognizer.recognize();
                if (result != null) {
                    String speech = result.getBestFinalResultNoFiller().toLowerCase();
                    System.out.println("You said: " + speech);
                    processCommand(speech);
                } else {
                    System.out.println("Could not understand, please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processCommand(String command) {
        if (command.contains("hello")) {
            System.out.println("Hello! How can I assist you?");
            speak("Hello! How can I assist you?");
        } else if (command.contains("time")) {
            String time = java.time.LocalTime.now().toString();
            System.out.println("Current time is: " + time);
            speak("The current time is " + time);
        } else if (command.contains("exit")) {
            System.out.println("Goodbye!");
            speak("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Command not recognized.");
            speak("Sorry, I didn't understand that.");
        }
    }

    private static void speak(String text) {
        new Thread(() -> {
            try {
                com.sun.speech.freetts.Voice voice = com.sun.speech.freetts.VoiceManager.getInstance().getVoice("kevin16");
                if (voice != null) {
                    voice.allocate();
                    voice.speak(text);
                    voice.deallocate();
                } else {
                    System.out.println("Voice not available.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

