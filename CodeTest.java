
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class CodeTest {
	
	private Map<String, List<Integer>> cMap;  //define Map
	
	private Locale locale;
	
	public CodeTest(Locale locale) {
		this.locale = locale;
		this.cMap = new TreeMap<String, List<Integer>>();    //Treemap gives sorted list
	}
	
	//Build Concordance
	public void buildConcordance(String text) 
  {
		
		System.out.println("buildConcordance text = "+text );
		
		BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(this.locale);
		BreakIterator wordIterator = BreakIterator.getWordInstance(this.locale);

		int sentenceIndex = 0;
		int sentenceCounter = 0;
		
		sentenceIterator.setText(text);
		
		//Iterate the sentence
		while (BreakIterator.DONE != sentenceIterator.next()) {
			String sentence = text.substring(sentenceIndex, sentenceIterator.current());
			sentenceCounter++;

			int wordIndex = 0;
			wordIterator.setText(sentence);

			// Go through words
			while (BreakIterator.DONE != wordIterator.next()) {
				String word = sentence.substring(wordIndex, wordIterator.current()).toLowerCase();

				if (Character.isLetterOrDigit(word.charAt(0))) {
					if (this.cMap.containsKey(word)) {
						this.cMap.get(word).add(sentenceCounter);
					} else {
						List<Integer> sentenceNumbers = new ArrayList<Integer>();
						sentenceNumbers.add(sentenceCounter);
						this.cMap.put(word, sentenceNumbers);
					}
				}
				wordIndex = wordIterator.current();
			}
			sentenceIndex = sentenceIterator.current();
		}
	}
	
	//Print output
	public void printResult() {
		for (String wordDisplay : this.cMap.keySet()) {
			List<Integer> sentenceNumbers = this.cMap.get(wordDisplay);
			System.out.println(wordDisplay + "  => " + sentenceNumbers.size() + " { " + sentenceNumbers.toString() + "}");
		}
	}

	public static void main(String[] args) 
  {
		
		System.out.println("Started Runnuing Main...");
		
		String fileName = (args.length == 0 ? "input.txt" : args[0]);
		
		System.out.println("fileName = "+fileName);
		
		Charset inputEncoding = StandardCharsets.UTF_8;

		try {
			String textContent = new String(Files.readAllBytes(Paths.get(fileName)), inputEncoding);
			
			System.out.println("Actual Input Content => " + textContent);
			
			if (textContent != null && !textContent.trim().isEmpty()) {
				
				CodeTest codeTest = new CodeTest(Locale.US);
				
				// Build concordance
				codeTest.buildConcordance(textContent);
				
				// Print Result
				codeTest.printResult();
				
			} else {
				throw new IllegalArgumentException();
			}
		} catch (IOException e) {
			System.out.println("Error: Could not load file --> " + fileName);
		} catch (IllegalArgumentException e) {
			System.out.println("Text file Name --> '" + fileName + "' is empty!");
		}
	}
}
