package miscellaneous;

import java.util.ArrayList;
import java.util.Random;

public class InsultGenerator {
	private ArrayList<String> adjective = new ArrayList<>();
	private ArrayList<String> insult = new ArrayList<>();
	private ArrayList<String> subjective = new ArrayList<>();

	public InsultGenerator() {
		this.initAdjectiveList();
		this.initInsultList();
		this.initSubjectList();
	}

	/**
	 * List originates from https://imgur.com/gallery/dXCGBE0
	 * @return one out of 6859 different insults
	 */
	public String getRandomInsult() {
		Random random = new Random();
		String insultString = "";
		insultString += adjective.get(random.nextInt(this.adjective.size()));
		insultString += " ";
		insultString += insult.get(random.nextInt(this.insult.size()));
		insultString += " ";
		insultString += subjective.get(random.nextInt(this.subjective.size()));
		return insultString;
	}

	private void initAdjectiveList() {
		String allAdjective = "LAZY,STUPID,INSECURE,IDIOTIC,SLIMY,SLUTTY,SMELLY,POMPOUS,COMMUNIST,DICKNOSE,PIE-EATING,RACIST,ELITIST,WHITE TRASH,DRUG-LOVING,BUTTERFACE,TONE DEAF,UGLY,CREEPY";
		String[] allAdjectiveAsArray = allAdjective.split(",");
		for (String oneAdjective : allAdjectiveAsArray) {
			this.adjective.add(oneAdjective);
		}
	}

	private void initInsultList() {
		String allInsults = "DOUCHE,ASS,TURD,RECTUM,BUTT,COCK,SHIT,CROTCH,BITCH,TURD,PRICK,SLUT,TAINT,FUCK,DICK,BONER,SHART,NUT,SPHINCTER";
		String[] allInsultsaAsArray = allInsults.split(",");
		for (String oneInsult : allInsultsaAsArray) {
			this.insult.add(oneInsult);
		}
	}

	private void initSubjectList() {
		String allSubjects = "PILOT,CANOE,CAPTAIN,PIRATE,HAMMER,KNOB,BOX,JOCKEY,NAZI,WAFFLE,GOBLIN,BLOSSUM,BISCUIT,CLOWN,SOCKET,MONSTER,HOUND,DRAGON,BALLOON";
		String[] allSubjectsAsArray = allSubjects.split(",");
		for (String oneSubject : allSubjectsAsArray) {
			this.subjective.add(oneSubject);
		}
	}
}
