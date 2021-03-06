// Imports the Google Cloud client library
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {

  /**
   * Demonstrates using the Speech API to transcribe an audio file.
   */
  public static void main(String... args) throws Exception {

    if ( args.length < 1) {
        System.err.println(String.format("args.length: %2d", args.length));
        System.err.println("audio file name is not specified.");
        return;
    }

    // Instantiates a client
    try ( SpeechClient speechClient = SpeechClient.create()) {

        // The path to the audio file to transcribe
        String fileName = args[0];

        // Reads the audio file into memory
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        ByteString audioBytes = ByteString.copyFrom(data);



        System.out.println(" Builds the sync recognize request");

        RecognitionConfig config = RecognitionConfig.newBuilder()
            .setEncoding(AudioEncoding.LINEAR16)
            .setSampleRateHertz(16000)
            .setLanguageCode("en-US")
            .build();

        System.out.println(" Builds the audio");
        RecognitionAudio audio = RecognitionAudio.newBuilder()
            .setContent(audioBytes)
            .build();


        System.out.println("Performs speech recognition on the audio file");

        RecognizeResponse response = speechClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = response.getResultsList();

        for (SpeechRecognitionResult result : results) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf(" Transcription: %s%n", alternative.getTranscript());
        }
    }
  }
}
