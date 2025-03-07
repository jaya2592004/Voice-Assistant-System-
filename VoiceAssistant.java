import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceAssistant {
    public static void main(String[] args) {
        try {
            // Configure Speech Recognition
            Configuration config = new Configuration();
            config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            config.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            config.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(config);
            recognizer.startRecognition(true);
            
            System.out.println("Voice Assistant is listening... Say something!");
            SpeechResult result;

            while ((result = recognizer.getResult()) != null) {
                String command = result.getHypothesis();
                System.out.println("You said: " + command);
                
                // Respond to commands
                if (command.equalsIgnoreCase("hello")) {
                    speak("Hello! How can I assist you?");
                } else if (command.equalsIgnoreCase("what is the time")) {
                    speak("The current time is " + java.time.LocalTime.now());
                } else if (command.equalsIgnoreCase("exit")) {
                    speak("Goodbye!");
                    break;
                } else {
                    speak("Sorry, I didn't understand that.");
                }
            }
            recognizer.stopRecognition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Text-to-Speech Function
    public static void speak(String text) {
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            voice.speak(text);
            voice.deallocate();
        } else {
            System.out.println("Voice not found!");
        }
    }
}
